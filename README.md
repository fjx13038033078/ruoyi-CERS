# 🎓 高考志愿智能推荐系统 | Smart Gaokao Advisor

![GitHub](https://img.shields.io/github/license/fjx13038033078/ruoyi-CERS)
![jdk version](https://img.shields.io/badge/JDK-17-blue.svg)
![node.js version](https://img.shields.io/badge/nodejs-16-orange.svg)
![GitHub Repo stars](https://img.shields.io/github/stars/fjx13038033078/ruoyi-CERS)

基于 SpringBoot + Vue 的高考志愿智能推荐系统，结合高校录取线、985/211 分布、院校排名和专业前景等多维数据，帮助你做出更明智的选择！

> 📌 如果你觉得项目不错，欢迎点个 Star ⭐ 支持一下！

---

## 🔍 项目简介

本项目适合作为计算机专业学生或对编程有兴趣的初学者的实践项目，具有以下特点：

- 技术适配性强：项目面向具备一定 Java 与 Vue 基础的开发者，能够帮助其巩固并提升全栈开发能力。

- 实践性突出：通过本项目，开发者可深入了解前后端分离架构的开发流程、技术选型与系统设计方法。

- 算法应用基础：项目引入了基础推荐算法的实战应用，帮助开发者理解算法在实际业务场景中的落地方式。

---

## 🚀 技术架构

- **前端**：Vue 2 + ECharts + Element UI
- **后端**：Spring Boot + MyBatis + MySQL + Redis
- **可视化**：中国地图热力图，展示高校资源分布
- **推荐算法**：基于分数线、地区、专业热度进行智能匹配，使用协同过滤和基于内容的推荐算法混合，解决没有行为记录情况下的冷启动问题（持续优化中）

---

## 🧩 核心功能

| 模块名称     | 描述                                                         |
|--------------|--------------------------------------------------------------|
| 🎓 地图可视化 | 展示全国各省 985/211 高校数量        |
| 🏫 院校查询   | 按地区、类型等维度查询全国高校信息                   |
| 📚 专业查询   | 查询专业的录取分数线，所属学科门类                       |
| 💡 智能推荐   | 根据考生分数、科类、意向地区推荐匹配的大学和专业           |
| 🛠️ 后台管理  | 支持高校、专业、分数线等数据的维护（管理员权限）          |

---

## 📸 系统截图

> 🚧 页面设计持续优化中，欢迎提交 PR 一起共建！

| 登录            | 注册          |
|----------------------|----------------------|
| ![login](./screenshots/login.png) | ![register](./screenshots/register.png) |

| 高校列表            | 专业列表          |
|----------------------|----------------------|
| ![university](./screenshots/university.png) | ![major](./screenshots/major.png) |

| 管理员首页            | 用户首页          |
|----------------------|----------------------|
| ![admin](./screenshots/index2.png) | ![user](./screenshots/index1.png) |


| 地图可视化            | 智能推荐页面          |
|----------------------|----------------------|
| ![map](./screenshots/map.png) | ![recommend](./screenshots/recommend.png) |



---

## 📦 快速启动

```bash
# 克隆项目
git clone https://github.com/fjx13038033078/ruoyi-CERS.git

# 导入后端 SpringBoot 项目，配置好数据库
# 导入前端 Vue 项目，运行 npm install & npm run dev

# 管理员账号：admin 密码：admin123
# 普通用户账号：zhangsan 密码：123456 
```


## 📬 联系作者

如果你对本系统感兴趣，或者有以下需求：

- 🌟 项目功能定制
- 🚀 二次开发协助
- 🧩 本地部署支持
- 🛠️ 系统优化与集成

欢迎联系作者洽谈合作！

**📧 联系方式：** `c1111111c@163.com`  
**💬 微信 ：** `445981543`  

---

## ☕ 支持作者

如果你觉得本项目对你有帮助，欢迎请作者喝杯咖啡，支持项目持续更新！🎉

<img src="screenshots/wechat_donate.png" alt="微信收款码" width="300" />



