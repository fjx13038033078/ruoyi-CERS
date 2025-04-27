
# CERS（College Entrance Examination Recommendation System的缩写）

## 内置功能

1. 基于协同过滤推荐算法+基于内容的推荐算法实现高考志愿个性化推荐系统，同时实现各省份高校信息地图显示。
2. 本系统可作为具备一定编程基础人员学习全栈技术的项目，也可以作为毕业设计的基础版本
3. 本系统完全开源，如果觉得不错请点个Star

## 部署与启动
1. npm install前需要换源，执行npm config set registry https://registry.npmmirror.com
2. 前端首次启动进入ruoyi-ui目录后，执行npm install即可
3. 如果node版本高于16，在idea终端打开进入ruoyi-ui目录后，执行 $env:NODE_OPTIONS="--openssl-legacy-provider" 后再执行npm run dev
4. 如果在用DBeaver执行数据库脚本时报错，则添加额外的命令参数 --default-character-set=utf8mb4
5. 安装echarts执行 npm install echarts -save

## 联系
1. 需要调试或者部署/二次开发/需求定制请+v:fjx2087046


