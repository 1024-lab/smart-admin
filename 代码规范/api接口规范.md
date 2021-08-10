### Restful接口规范
1、不推荐使用 rest 命名 url， 只能使用 get/post 方法。url 命名上规范如下：虽然 Rest 大法好，但是有时并不能一眼根据 url看出来是什么操作。
url 格式为： 

/业务模块/子模块/动作

举例：
```
GET  /department/get/{id}      查询某个部门详细信息
POST /department/query         复杂查询
POST /department/add           添加部门
POST /department/update        更新部门
GET  /department/delete/{id}   删除部门
```
### controller 里接口 swagger 规范
1、controller每个方法必须添加 swagger 文档注解 @ApiOperation ，并填写接口描述信息，描述最后必须加上作者信息 @author 哪吒 
比如：
```java
  @ApiOperation("更新部门信息 @author 哪吒")
    @PostMapping("/department/update")
    public ResponseDTO<String> updateDepartment(@Valid @RequestBody DeptUpdateDTO deptUpdateDTO) {
        return departmentService.updateDepartment(deptUpdateDTO);
    }
```

### controller 里 传入和返回 JavaBean 规范
1、类中的每个字段添加注释
2、对于枚举值的类型，要清楚标注清楚可能的值，以及每个值表示什么含义
比如：
```
public class UserVO{

    @ApiModelProperty("性别：0表示 女，1表示 男，2表示 未知")
    private Integer gender;

}

```