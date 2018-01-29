# PlanH V2 实体 描述文档

* [管理员](#管理员)
* [用户](#用户)
  * [用户设置](#用户设置)
* [团队](#团队)
* [科目](#科目)
* [任务](#任务)
* [提交](#提交)
  * [提交文件属性](#提交文件属性)
* [资料文档](#资料文档)
* [格式化处理器](#格式化处理器)

---

### 管理员
完全限定名：
`top.kanetah.planhv2.api.entity.Admin`  

属性：
- adminId `Int` 管理员Id
- password `String` 管理员口令

例：
```
{
  adminId: 1,
  password: 123,
}
```

---

### 用户
完全限定名：
`top.kanetah.planhv2.api.entity.User`  

属性：
- userId `Int` 用户Id
- userCode `String` 用户代码
- userName `String` 用户名
- userConfig `UserConfig` [用户设置](#用户设置)
- accessToken `String?` 用户AccessToken

>#### 用户设置
>完全限定名
>`top.kanetah.planhv2.api.entity.UserConfig`  
>
>属性：
>- theme `String?` 主题
>- accessByToken `Boolean` 能否通过令牌直接访问

例：
```
{
  userId: 55,
  userCode: 1521192255,
  userName: "某某",
  config: {
    theme: "dark",
    accessByToken: false,
  },
  accessToken: null,
}
```

---

### 团队
完全限定名：
`top.kanetah.planhv2.api.entity.Team`  

属性：
- teamId `Int` 团队Id
- subjectId `Int` 科目Id
- teamIndex `Int` 团队编号
- teamName `String?` 团队名称
- memberUserIdArray `IntArray` 团队成员数组
- leaderUserIdArray `IntArray` 团队领导数组

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

### 科目
完全限定名：
`top.kanetah.planhv2.api.entity.Subject`  

属性：
- subjectId `Int` 科目Id
- subjectName `String` 科目名称
- teacherName `String` 教师姓名
- emailAddress `String` 邮箱地址
- teamLimit `IntArray?` 科目团队人数限制
- recommendProcessorId `Int` 推荐格式化处理器Id

例：
```
{
  subjectId: 1,
  subjectName: "音乐",
  teacherName: "赵",
  emailAddress: "123456789@hehe.com",
  teamLimit: [0, 7, 8]
  recommendProcessorId: 1,
}
```

---

### 任务
完全限定名：
`top.kanetah.planhv2.api.entity.Task`  

属性：
- taskId `Int` 任务Id
- subjectId `Int` 科目Id
- title `String` 标题
- content `String` 内容
- isTeamTask `Boolean` 是否团队任务
- deadline `Timestamp` 截止日期
- type `String` 文件类型
- formatProcessorId `Int` 存储处理器Id
- format `String?` 存储格式

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
  formatProcessorId: 1,
  format: "[code]_[name]",
}
```

---

### 提交
完全限定名：
`top.kanetah.planhv2.api.entity.Submission`  

属性：
- submissionId `Int` 提交Id
- taskId `Int` 任务Id
- userId `Int` 用户Id
- teamId `Int?` 团队Id
- submitData `Timestamp` 提交时间
- fileAttributes `SubmitFileAttributes` [提交文件属性](#提交文件属性)

>#### 提交文件属性
>完全限定名：
>`top.kanetah.planhv2.api.entity.SubmitFileAttributes`  
>
>属性：
>- formerName `String` 原文件名
>- saveName `String` 保存文件名
>- size `Double` 文件大小
>- path `String` 文件存储路径

例：
```
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
}
```

---

### 资料文档
完全限定名：
`top.kanetah.planhv2.api.entity.Resource`  

属性：
- resourceId `Int` 资料文档Id
- resourceName `String` 资料文档名称
- resourceSize `Double` 资料文档大小
- resourceUrl `String` 资料文档Url

例：
```
{
  resourceId: 1,
  resourceName: "example.zip",
  resourceSize: 1000000,
  resourceUrl: "https://api.planhv2.kanetah.top/resource/example.zip"
}
```

---

### 格式化处理器
完全限定名：
`top.kanetah.planhv2.api.entity.FormatProcessor`  

属性：
- formatProcessorId `Int` 格式化处理器Id
- formatProcessorName `String` 格式化处理器名称
- formatProcessorClassName `String` 格式化处理器完全限定名

例：
```
{
  formatProcessorId: 1,
  formatProcessorName: "最外层文件名格式化处理器",
  formatProcessorClassName: "top.kanetah.planhv2.api.format.processor.OutsideFileNameFormatProcessor",
}
```
