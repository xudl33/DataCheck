# DataCheck
DataCheck是一个基于Spring的数据校验框架

1. 在PO中声明`@Check`注解
``` java
public class RegisterUserPo {
    @ApiModelProperty(value = "登录名", required = true)
    @Check(test = "required")
    private String loginName;

    @ApiModelProperty(value = "密码", required = true)
    @Check(test = "required")
    private String password;
}
```
2. 在任意方法前加入`@DataCheck`注解，都会进行校验,例如在controller中可以这样写
``` java
@Api(tags = "用户注册接口")
@RestController
public class RegisterController {
    @Autowired
    private UserService userService;

    /**
     * 注册用户
     * 
     * @return
     */
    @ApiOperation(value = "注册用户", notes = "根据User对象注册用户(如果有拓展表，需要在extend中设置)")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @DataCheck
    public ResultPoJo<User> register(@RequestBody RegisterUserPo registerUser) {
        return userService.insert(registerUser);
    }
}
```
3. 如果参数为基本类型，也可以直接把`@Check`加在Parameter上，例如:`
``` java
/**
 * 批量删除用户信息
 * 
 * @param ids
 * @return
 */
@PreAuthorize("hasRole('admin')")
@ApiOperation(value = "user-018_【用户】批量删除用户信息", notes = "批量删除用户信息，权限：ROLE_admin")
@ApiImplicitParam(name = "ids", value = "用户.id字符串数组(逗号分隔)", required = true, dataType = "String")
@RequestMapping(value = "batDelete", method = RequestMethod.POST)
@DataCheck
public ResultPoJo<Object> batDelete(@Check(test = "required") @RequestBody String ids) {
	return userService.batDelete(ids);
}
```
4. 如果参数为List或Map类型，那么会进行遍历校验，例如:`
``` java
/**
 * 批量修改用户有效期
 * 
 * @param effeBatUpdateList
 * @return
 */
@PreAuthorize("hasRole('admin')")
@ApiOperation(value = "user-017_【用户】批量修改用户有效期", notes = "批量修改用户有效期，权限：ROLE_admin")
@RequestMapping(value = "effe/batUpdate", method = RequestMethod.POST)
@DataCheck
public ResultPoJo<Object> effeBatUpdate(@RequestBody List<EffeBatUpdatePo> effeBatUpdateList) {
	return userService.effeBatUpdate(effeBatUpdateList);
}
```
``` java
/**
 * 修改用户有效期PO
 */
public class EffeBatUpdatePo {
    @ApiModelProperty(value = "用户.id 非空", required = true)
    @Check(test = "required")
    private Long id;

    @JsonSerialize(using = OffsetDateTimeSerializer.class)
    @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
    @ApiModelProperty(value = "有效期 若为空则代表长期有效", required = true)
    private OffsetDateTime availableDate;
	// ...
}
```
如果这样标准的校验方式不能满足要求，也可以在service中实现校验逻辑
5. 也可以在Service中，如果基层了`BaseService`，可以对Po进行校验并设置ResultPojo的错误信息
``` java
@Service
public class UserService extends BaseService {
    @Transactional(readOnly = false)
    public ResultPoJo<Object> loginBatUpdate(LoginBatUpdatePo loginBatUpdate) {
        ResultPoJo<Object> result = new ResultPoJo<Object>();
        // 进行数据校验
        if (!dataCheck(result, loginBatUpdate)) {
            return result;
        }
        // ...
       return result;
	}
}
```
6.  也可以直接使用`DataCheckUtil`的方法进行校验
``` java
public boolean dataCheck(Object entity) {
	List<String> checkList = DataCheckUtil.checkResultMsg(entity);
	if (ConverterUtil.isNotEmpty(checkList)) {
               // ...
		return false;
	}
	return true;
}
```
这里的DataCheckUtil.checkResultMsg只是例子，如果不需要返回错误消息，可以直接使用`DataCheckUtil.check`函数

7. 也可以使用非注解的方式，用Map进行校验格式的声明,`Map(DataCheck)的方式，不支持logic校验，因为可以使用logic的方式实现，因而不再使用Map的校验方式`
``` java
public static final Map<String, DataCheck> USER_PASSWORD_CHECK_MAP = new HashMap<String, DataCheck>() {
        private static final long serialVersionUID = 8136871463161977654L;
        {
            put("id", new DataCheck(new String[] { "required" }));
            put("password", new DataCheck(new String[] { "required" }));
            put("newPassword", new DataCheck(new String[] { "required" }));
        }
    };
public boolean dataCheck(Object entity) {
	List<String> checkList = DataCheckUtil.checkResultMsg(entity,  USER_PASSWORD_CHECK_MAP);
	if (ConverterUtil.isNotEmpty(checkList)) {
               // ...
		return false;
	}
	return true;
}
```
8. 若被校验的属性为一个`bean`对象，那么需要声明`cascade=true`，这样的校验被称为级联校验;
``` java
public class TestPo {
    @Check(test = "required")
    private String code;

    @Check(test = "required", cascade = true)
    private TestPo2 test;
	// ...
}
```
``` java
public class TestPo2 {
    @Check(test = "required")
    private String name;
	// ...
}
```
9. 若遇到需要更为复杂的校验逻辑，必须要手动进行check时，可以选择logic:逻辑表达式的方式进行校验，若需要使用父节点的属性，则需要使用`parent[xxx]`，若级联的层次较深，表达式的中二级以上的`parent`需要写为`[parent]`。
``` java
public class TestPo {
    @Check(test = "required")
    private String code;

    @Check(test = "logic", logic = "#isNotEmpty(#code, #name)")
    private String name;

    @Check(test = "required", cascade = true)
    private TestPo2 test;
	// ...
}
```
``` java
public class TestPo2 {
    //@Check(test = {"required", "logic"}, logic = "'1'.equals(#parent[code])") // 必填且parent.code=1
    @Check(test = "logic", logic = "'1'.equals(#parent[code])?#isNotEmpty(#code):true") // parent.code=1时则该项必填
    private String code;
    @Check(test = "logic", logic = "'1'.equals(#parent[code])?#isNotEmpty(#name):true")
    private String name;
    
    @Check(test = "required", cascade = true)
    private TestPo3 lop;
	// ...
}
```
``` java
public class TestPo3 {
    @Check(test = "logic", logic = "'1'.equals(#parent[parent][code])?#isNotEmpty(#mem):true")
    private String mem;
	
	// ...
}
```
10. 若需要使用自定义的校验函数，则可以将方法声明成static,使用`T(AllPackage).xxx`的方式进行调用。
``` java
public final class ConverterUtil {
    public static boolean isNotEmpty(Object str) {
	    //...
        return !isEmpty(str);
    }
}
```
``` java
public class TestPo3 {
    @Check(test = "logic", logic = "T(com.wisea.cloud.common.util.ConverterUtil).isNotEmpty(#asa)")
    private String asa;
	// ...
}
```
11. 若希望某些通用的校验函数直接能够在logic中使用，则可以通过设置注解@CheckModel和@CheckApi进行设置，这样就可以使用了
``` java
@CheckModel
public final class ConverterUtil {
    @CheckApi
    public static boolean isNumeric(Object... strs) {
        boolean result = true;
        for (Object obj : strs) {
            result = (result && StringUtils.isNumeric(toString(obj, "")));
        }
        return result;
    }
}
```
``` java
public class TestPo4 {
    @Check(test = "logic", logic = "#isNumeric(#num)")
    private Integer num;
	// ...
}
```

`DataCheck`支持以下八种常用的校验格式，更具体的说明请参考`@Check`注解

required:非空,length标准长度,mixLength中英文长度,minLength:最小长度,maxLength:最大长度,liveable:有效性,regex:正则表达式,logic:逻辑表达式。logic:逻辑表达式的语法请参考官方文档，除此之外还增加了isEmpty和isNotEmpty两个常用的判断函数。`Map(DataCheck)的方式，不支持logic校验，因为可以使用logic的方式实现，因而不再使用Map的校验方式`

[Spring Expression Language 官方文档](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions)
