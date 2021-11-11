# ![](https://cdn.interbank.pe/halcon-renovacion-v2-theme/images/favicon.ico) Jarvis NRI Distributed Tracing

In this section we look about the Jarvis NRI Distributed Tracing. <br />
This library was written in Java with Spring Boot Framework.

### Methods

|Name|Type|Return type|Parameters|Description|
|------------|-----|--------------|----|--------------------------------|
|DistributedTracingNR|Annotation|None|componentType <br /> componentName <br /> applicationName|Log the headers and params sent in request operation|

Where:

* componentType: Component type to log
* componentName: Component name to log
* applicationName: Microservice name to log

### How to use

```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class dummyClass {

    @GetMapping("/hello")
    @DistributedTracingNR(componentType = "MS", componentName = "microservice-credit-card-detail-movements", applicationName = "credit-card-detail-movements-ms")
    public String helloWorld() {
        return "hello";
    }

    @LogExecutionTime
    private testLogger() {
        // algorithm
    }
}
```