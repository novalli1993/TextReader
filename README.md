# 简介
简单的txt阅读器。目前仅支持本地txt文件阅读，暂时也没有解决如何打包为.exe独立运行的问题。
# 具体细节
- 由于这是做Android之前的练手工程，我大致按照MVVM架构进行了组织。
  - View
    - 在该类中使用声明式搭建了基本GUI。所有组件都尽量以Java中已有的数据类型进行数据传递。
  - View Model
    - 在该类中主要是将数据类与GUI相互连接，主要工作就是根据GUI提供的信息将数据类转化为接近最终呈现的数据类型。
  - Model
    - 分为数据和数据处理两大类。
    - 数据类主要是定义了“段落”、“章节”和“书”三个主要数据类型。
    - 数据处理主要是实现了txt文件导入和本地缓存文件读取两个功能。
  - Start
    - 由于JavaFX的要求，需要使用一个独立的Start类启动Application实例。
  - resource
    - 主要资源只有logo和一个css文件。声明式UI仍然依赖css命令进行界面美化，因此css文件是必要的，且有利于统一管理样式。
- 部分思考
  - 在搭建GUI时，尽量通过get和set函数进行组件搭建，以方便后期统一控制组件的基础样式。
  - 文件导入后不直接进行输出，而是整理好格式后保存为临时文件后再进行读取。
  - 临时文件的读取略慢，需要优化。
# 致谢
该项目主要参考了CatReader
[Blog](https://www.cnblogs.com/capterlliar/p/16211347.html)
[Code](https://github.com/capterlliar/CatReader.git)

具体技术细节大量“咨询”了[人工智能模型kimi](https://kimi.moonshot.cn/)

Java部分内容的“复习”主要仰赖[菜鸟教程](https://www.runoob.com/)和[廖雪峰老师](https://www.liaoxuefeng.com/wiki/1252599548343744)