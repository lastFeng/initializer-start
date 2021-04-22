### 标准方言
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymleaf.org">
<span th:text="..." />
<span data-th-text="..."/>
<html>
```

#### 标准表达式
```thymeleafexpressions
变量表达式: ${param}
消息表达式: #{param}
选择表达式: *{param} - 遍历使用
连接表达式: @{...}
分段表达式: th:insert|th:replace
字面量：单引号来表示字符 - 数字 - 算数操作（+-*/%）
```

#### 设置属性值
```thymeleafexpressions
设置任意或者指定属性值： th:attr
```

#### 迭代器
 - th:each  

#### 条件语句
- th:if
- th:unless
- th:switch - th:case

#### 模板布局

#### 属性优先级

#### 注释

#### 内联

#### 基本对象

#### 工具对象


