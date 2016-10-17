启动远程调试
--------------
mvn clean package
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar target/my-first-app-1.0-SNAPSHOT-fat.jar

使用配置文件（修改端口号）启动
--------------
java -jar target/my-first-app-1.0-SNAPSHOT-fat.jar -conf src/main/conf/my-application-conf.json

Maven单元测试、集成测试
--------------
mvn clean verify


## 单元测试的Maven配置
### 依赖包
1. vertx-core:test-jar
2. vertx-unit
3. junit
4. jbehave-core
5. jbehave-junit-runner
6. jmockit (mock工具)

## 集成测试的Maven配置
### 依赖包(dependency/artifactId)
1. rest-assured
2. assertj-core

### 插件配置(artifactId)
1. maven-shade-plugin
2. build-helper-maven-plugin (自动发现可用的端口号，并写入到配置文件中)
3. maven-antrun-plugin
4. maven-failsafe-plugin

### 其他POM配置
<pre><code>build/testResources/testResource</code></pre>


## YAML文档生成的Maven配置
### API文档位置
<pre><code>${project.basedir}/src/main/swagger</code></pre>

### 插件配置(artifactId)
1. swagger2markup-maven-plugin (YAML转换为Asciidoc文本)
2. asciidoctor-maven-plugin (Asciidoc转换为html、pdf)
3. maven-resources-plugin (将html、pdf拷贝到指定目录下)