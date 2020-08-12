# the-Wind-of-Rain
java project
手写IOC与SpringMvc总结
1.SpringMVC
(1)自定义注解 @interface (SunController,SunService....等等）
(2)完成Controller service 开发（标上自己的注解）
(3)实现DispatcherServlet继承HttpServlet
A.在web.xml中配置DispatcherServlet
B.DispatcherServlet的初始化
       扫描项目下的所有包，获取所有类名加到list中
       判断类上是否标注@SunController,@SunServie注解，通过反射创建这些类，并加入到HashMap中（IOC）容器。
       扫描IOC容器中的类来完成依赖注入
       解析URL路径，创建一个HashMap作为处理器映射器(handers)，key是路径，value是对应的处理方法。
C.重写Dopost方法实现主要逻辑
       从Request获取映射路径,
       从handers中找到路径对应的处理方法
       根据路径从IOC容器中取得对应的Controller;
       通过反射调用处理方法执行。
