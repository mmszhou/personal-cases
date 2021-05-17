策略模式

我自己的理解：

什么是策略模式？（what）
当一个业务场景，需要根据不同的情况，执行不同的方法的时候，就需要用到策略模式。
可以避免很多冗长的if else分支。

如何实现？(how)
首先定义一个策略接口，不同的实现方式，分别继承这一个接口，并单独实现接口的方法。
使用工厂模式，在spring启动时，把实现了所有策略接口实现类的对象，放到一个map中。

使用是，根据不同的请求参数，获取不同的实例。



资料方面正式的解释：

策略模式包含一个策略接口，和一组实现这个接口的策略类。
所有的策略类都实现相同的接口，所以，客户端基于接口而非实现编程。
可以灵活的替换不同的策略。