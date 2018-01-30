# PlanH V2 API 描述文档

本文档内api路径皆以 https://api.planhv2.kanetah.top 为前缀

* [管理员](#管理员)
* [用户](#用户)
* [团队](#团队)
* [科目](#科目)
* [任务](#任务)
* [提交](#提交)
* [资料文档](#资料文档)
* [格式化处理器](#格式化处理器)

---

## 管理员
### `post` /authorized 管理员鉴权
参数：
- password `String` 管理员口令
- validate `String` 人机身份验证

返回：
管理员登录结果、管理员鉴权码  
例：
```
{
  success: false,
  authorized: null,
}
```

### `delete` /authorized 注销鉴权
参数：
- authorized `String` 管理员鉴权码

返回：
注销鉴权结果  
例：
```
{
  success: true,
}
```

### `get` /getAllAdmins 管理员列表
参数：
- authorized `String` 管理员鉴权码

返回：
管理员列表（不包括密码）  
例：
```
[
{
  adminId: 1,
},
{
  adminId: 2,
},
...
]
```

### `post` /admin 新建管理员
参数：
- authorized `String` 管理员鉴权码
- password `String` 新管理员口令

返回：
新建结果  
例：
```
{
  success: true,
}
```

### `delete` /admin/:id 删除管理员
参数：
- authorized `String` 管理员鉴权码
- _adminId_ `Int` 管理员Id `PathVariable:id`

返回：
删除结果  
例：
```
{
  success: true,
}
```

### `get` /admin/:id 查找管理员
参数：
- authorized `String` 管理员鉴权码
- _adminId_ `Int` 管理员Id `PathVariable:id`

返回：
查找结果  
例：
```
{
  adminId: null,
}
```

---

## 用户
### `post` /token 用户登录
参数：
- userCode `String` 用户代码
- userName `String` 用户名

返回：
用户登录结果、用户Token  
例：
```
{
  success: false,
  token: null,
}
```

### `delete` /token 注销登录
参数：
- token `String` 用户Token

返回：
注销登录结果  
例：
```
{
  success: true,
}
```

### `get` /user 用户详情
参数：
- token `String` 用户Token

返回：
用户详情内容
例：
```
{
  userId: 55,
  userCode: 1521192255,
  userName: "某某",
  config: {
    theme: "dark",
    enableAccessToken: false,
  },
  accessToken: null,
}
```

### `patch` /user/:id 用户设置
参数：
- token `String` 用户Token
- _userId_ `Int` 用户Id `PathVariable:id`
- theme `String?` 主题样式
- enableAccessToken `Boolean?` 能否通过令牌直接访问

返回：
修改结果
(仅当enableAccessToken被修改为true时返回访问令牌内容)  
例：
```
{
  success: false,
  accessToken: null,
}
```

### `get` /users 用户列表
参数：
- token `String` 用户Token

返回：
所有用户（不包括config与accessToken）  
例：
```
[
{
  userId: 55,
  userCode: 1521192255,
  userName: "某某",
},
{
  userId: 66,
  userCode: 1521192266,
  userName: "某某",
},
...
]
```

### `post` /user 新建用户
参数：
- authorized `String` 管理员鉴权码
- userCode `String` 用户代码
- userName `String` 用户名

返回：
新建结果  
例：
```
{
  success: true,
}
```

### `delete` /user/:id 删除用户
参数：
- authorized `String` 管理员鉴权码
- _userId_ `Int` 用户Id `PathVariable:id`

返回：
删除结果  
例：
```
{
  success: true,
}
```

### `put` /user/:id 修改用户
参数：
- authorized `String` 管理员鉴权码
- _userId_ `Int` 用户Id `PathVariable:id`
- userCode `String?` 用户代码
- userName `String?` 用户名

返回：
修改结果  
例：
```
{
  success: true,
}
```

### `get` /user/:id 用户详情
参数：
- authorized `String` 管理员鉴权码
- _userId_ `Int` 用户Id `PathVariable:id`

返回：
用户详情内容  
例：
```
{
  userId: 55,
  userCode: 1521192255,
  userName: "某某",
  config: {
    theme: "dark",
    enableAccessToken: false,
  },
  accessToken: null,
}
```

---

## 团队
### `get` /teams 团队列表
参数：
- token `String?` 用户Token

返回：
用户所在的所有团队，当token为空时返回系统内所有团队  
例：
```
[
{
  teamId: 1,
  subjectId: 1,
  teamIndex: 1,
  teamName: "骑士团",
  memberUserIdArray: [1, 2],
  leaderUserIdArray: [1],
},
{
  teamId: 2,
  subjectId: 2,
  teamIndex: 1,
  teamName: "兄弟会",
  memberUserIdArray: [1, 2],
  leaderUserIdArray: [1, 2],
}
]
```

### `post` /team 创建团队
参数：
- token `String` 用户Token
- subjectId `Int` 科目Id
- teamName `String?` 团队名称
- memberUserIdArrayJsonString `String` 团队成员数组Json
- leaderUserIdArrayJsonString `String` 团队领导数组Json

返回：
创建结果  
例：
```
{
  success: true,
}
```

### `delete` /team/:id 删除团队
参数：
- token `String` 用户Token
- _teamId_ `Int` 团队Id `PathVariable:id`

返回：
删除结果  
例：
```
{
  success: true,
}
```

### `put` /team/:id 修改团队
参数：
- token `String` 用户Token
- _teamId_ `Int` 团队Id `PathVariable:id`
- subjectId `Int` 科目Id
- teamName `String?` 团队名称
- memberUserIdArrayJsonString `String?` 团队成员数组Json
- leaderUserIdArrayJsonString `String?` 团队领导数组Json

返回：
修改结果  
例：
```
{
  success: true,
}
```

### `get` /team/:id 团队详情
参数：
- token `String` 用户Token
- _teamId_ `Int` 团队Id `PathVariable:id`

返回：
团队详情
例：
```
{
  teamId: 1,
  subjectId: 1,
  teamIndex: 1,
  teamName: "骑士团",
  memberUserIdArray: [1, 2],
  leaderUserIdArray: [1],
}
```

---

## 科目
### `get` /subjects 科目列表
返回：
所有科目详情列表  
例：
```
[
{
  subjectId: 1,
  subjectName: "音乐",
  teacherName: "赵",
  emailAddress: "123456789@hehe.com",
  teamLimit: [0, 7, 8],
  recommendProcessorId: 1,
},
{
  subjectId: 2,
  subjectName: "美术",
  teacherName: "李",
  emailAddress: "987654321@hehe.com",
  teamLimit: [0, 5, 7],
  recommendProcessorId: 1,
},
...
]
```

### `post` /subject 新建科目
参数：
- authorized `String` 管理员鉴权码
- subjectName `String` 科目名称
- teacherName `String` 教师姓名
- emailAddress `String` 邮箱地址
- teamLimit `String?` 团队人数限制数组Json
- recommendProcessorId `Int` 推荐格式化处理器Id

返回：
新建结果  
例：
```
{
  success: true,
}
```

### `delete` /subject/:id 删除科目
参数：
- authorized `String` 管理员鉴权码
- _subjectId_ `Int` 科目Id `PathVariable:id`

返回：
删除结果  
例：
```
{
  success: true,
}
```

### `put` /subject/:id 修改科目
参数：
- authorized `String` 管理员鉴权码
- _subjectId_ `Int` 科目Id `PathVariable:id`
- subjectName `String` 科目名称
- teacherName `String` 教师姓名
- emailAddress `String` 邮箱地址
- teamLimit `String?` 团队人数限制数组Json
- recommendProcessorId `Int` 推荐格式化处理器Id

返回：
修改结果  
例：
```
{
  success: true,
}
```

### `get` /subject/:id 科目详情
参数：
- _subjectId_ `Int` 科目Id `PathVariable:id`

返回：
科目详情  
例：
```
{
  subjectId: 1,
  subjectName: "音乐",
  teacherName: "赵",
  emailAddress: "123456789@hehe.com",
  teamLimit: [0, 7, 8],
  recommendProcessorId: 1,
}
```

---

## 任务
### `get` /tasks 任务列表
参数：
- userId `Int` 用户Id
- subjectId `Int?` 科目Id
- page `Int` 页码
- limit `Int` 每一页的任务数量

返回：
任务内容详情列表  
例：
```
[
{
  taskId: 1,
  subjectId 1,
  title: "音乐作业",
  content: "录一首歌",
  isTeamTask: false,
  deadline: "2018/1/1 12:00:00",
  type: ".mp3, .mp4",
  format: "[code]_[name]",
  formatProcessorId: 1,
},
{
  taskId: 2,
  subjectId 2,
  title: "美术作业",
  content: "画一幅画",
  isTeamTask: false,
  deadline: "2018/1/1 12:00:00",
  type: ".jpg",
  format: null,
  formatProcessorId: 2,
},
...
]
```

### `post` /task 新建任务
参数：
- authorized `String` 管理员鉴权码
- subjectId `Int` 科目Id
- title `String` 标题
- content `String` 内容
- isTeamTask `Boolean` 是否团队任务
- deadline `Timestamp` 截止日期
- type `String` 文件类型
- format `String?` 存储格式
- formatProcessorId `Int` 存储处理器Id

返回：
新建结果  
例：
```
{
  success: true,
}
```

### `delete` /task/:id 删除任务
参数：
- authorized `String` 管理员鉴权码
- _taskId_ `Int` 任务Id `PathVariable:id`

返回：
删除结果  
例：
```
{
  success: true,
}
```

### `put` /task/:id 修改任务
参数：
- authorized `String` 管理员鉴权码
- _taskId_ `Int` 任务Id `PathVariable:id`
- subjectId `Int` 科目Id
- title `String` 标题
- content `String` 内容
- isTeamTask `Boolean` 是否团队任务
- deadline `Timestamp` 截止日期
- type `String` 文件类型
- format `String?` 存储格式
- formatProcessorId `Int` 存储处理器Id

返回：
修改结果  
例：
```
{
  success: true,
}
```

### `get` /task/:id 任务详情
参数：
- _taskId_ `Int` 任务Id `PathVariable:id`

返回：
任务详情  
例：
```
{
  taskId: 1,
  subjectId 1,
  title: "音乐作业",
  content: "录一首歌",
  isTeamTask: false,
  deadline: "2018/1/1 12:00:00",
  type: ".mp3, .mp4",
  format: "[code]_[name]",
  formatProcessorId: 1,
}
```

---

## 提交
### `get` /submissions 提交列表
参数：
- token `String` 用户Token

返回：
提交日志列表  
例：
```
[
{
  submissionId: 1,
  taskId: 1,
  userId: 55,
  teamId: null,
  submitDate: "2018/01/01 10:00:00",
  fileAttributes: {
    formerName: "poi.mp3",
    saveName: "1521192255_某某.mp3",
    size: 1000000,
    path: "/planHFile/音乐/音乐作业/poi.mp3",
  },
},
{
  submissionId: 2,
  taskId: 2,
  userId: 55,
  teamId: null,
  submitDate: "2018/01/01 10:00:00",
  fileAttributes: {
    formerName: "poi.jpg",
    saveName: "poi.jpg",
    size: 1000000,
    path: "/planHFile/美术/美术作业/poi.jpg",
  },
},
...
]
```

### `post` /submission 提交任务文件
参数：
- token `String` 用户Token
- taskId `Int` 任务Id
- file `File` 任务文件
- teamId `Int?` 团队Id

返回：
提交结果  
例：
```
{
  success: true,
}
```

### `delete` /submission 删除任务文件
参数：
- token `String` 用户Token
- taskId `Int` 任务Id

返回：
删除结果  
例：
```
{
  success: true,
}
```

### `get` /submission 下载任务文件
参数：
- token `String` 用户Token
- taskId `Int` 任务Id

返回：
任务文件

---

## 资料文档
### `get` /resources 资料文档列表
返回：
可下载的资料文件详情列表  
例：
```
[
{
  resourceId: 1,
  resourceName: "example.zip",
  resourceSize: 1000000,
  resourceUrl: "https://api.planhv2.kanetah.top/resource/example.zip"
},
{
  resourceId: 1,
  resourceName: "other.zip",
  resourceSize: 1000000,
  resourceUrl: "https://api.planhv2.kanetah.top/resource/other.zip"
},
...
]
```

### `post` /resource 上传资料文档
参数：
- token `String` 用户Token
- file `File` 资料文件

返回：
资料上传结果与资料Url  
例：
```
{
  success: true,
  resourceUrl: "https://api.planhv2.kanetah.top/resource/example.zip",
}
```

### `delete` /resource/:id 删除资料文档
参数：
- authorized `String` 管理员鉴权码
- _resourceId_ `Int` 资料文档Id `PathVariable:id`

返回：
资料删除结果  
例：
```
{
  success: true,
}
```

### `get` /resource/:filename 下载资料
返回：
资料文件

---

## 格式化处理器
### `get` /format/processor 格式化处理器列表
参数：
- authorized `String` 管理员鉴权码

返回：
格式处理器详情列表  
例：
```
[
{
  formatProcessorId: 1,
  formatProcessorName: "最外层文件名格式化处理器",
  formatProcessorClassName: "top.kanetah.planhv2.api.format.processor.OutsideFileNameFormatProcessor",
},
{
  formatProcessorId: 2,
  formatProcessorName: "最外层原文件名处理器",
  formatProcessorClassName: "top.kanetah.planhv2.api.format.processor.OutsideOriginalFileNameFormatProcessor",
},
...
]
```

### `post` /format/processor 添加格式化处理器
参数：
- authorized `String` 管理员鉴权码
- formatProcessorName `String` 格式化处理器名称
- formatProcessorClassName `String` 格式化处理器完全限定名
- processorCode `String` 格式化处理器代码

返回：
添加结果  
例：
```
{
  success: true,
}
```

### `delete` /format/processor/:id 删除格式化处理器
参数：
- authorized `String` 管理员鉴权码
- _formatProcessorId_ `Int` 格式化处理器Id `PathVariable:id`

返回：
删除结果  
例：
```
{
  success: true,
}
```

### `get` /format/processor/:id 格式化处理器详情
参数：
- authorized `String` 管理员鉴权码
- _formatProcessorId_ `Int` 格式化处理器Id `PathVariable:id`

返回：
格式化处理器详情  
例：
```
{
  formatProcessorId: 1,
  formatProcessorName: "最外层文件名格式化处理器",
  formatProcessorClassName: "top.kanetah.planhv2.api.format.processor.OutsideFileNameFormatProcessor",
}
```
