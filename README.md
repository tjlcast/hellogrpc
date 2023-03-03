# hellogrpc

演示maven 使用 protobuf 和 grpc.

## pom核心依赖

```aidl
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-netty-shaded</artifactId>
            <version>1.29.0</version>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-protobuf</artifactId>
            <version>1.29.0</version>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-stub</artifactId>
            <version>1.29.0</version>
        </dependency>
```

## 配置proto插件build
os-maven-plugin：此插件可以检测当前系统信息 ${os.detected.classifier}：这个变量获取操作系统的版本，例如osx-x86_64
该build默认protobuf文件路径为`src/main/proto`
<outputDirectory>：指定protobuf生成的java包路径
```aidl
    <build>
        <extensions>
            <extension>
                <groupId>kr.motd.maven</groupId>
                <artifactId>os-maven-plugin</artifactId>
                <version>1.6.2</version>
            </extension>
        </extensions>
        <plugins>
            <plugin>
                <groupId>org.xolstice.maven.plugins</groupId>
                <artifactId>protobuf-maven-plugin</artifactId>
                <version>0.6.1</version>
                <configuration>
                    <protocArtifact>com.google.protobuf:protoc:3.11.0:exe:${os.detected.classifier}</protocArtifact>
                    <pluginId>grpc-java</pluginId>
                    <pluginArtifact>io.grpc:protoc-gen-grpc-java:1.29.0:exe:${os.detected.classifier}</pluginArtifact>
                    <outputDirectory>${project.build.sourceDirectory}</outputDirectory>
                    <clearOutputDirectory>false</clearOutputDirectory>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                            <goal>compile-custom</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

## proto文件提示
在proto文件中指定java文件的package，如下所示：
```aidl
option java_package = "com.jialtang.hellogrpc.helloworld";
```

## 依赖打包
```aidl
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>2.4.1</version>
                <configuration>
                    <!-- get all project dependencies -->
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                    <!-- MainClass in mainfest make a executable jar -->
                    <archive>
                        <manifest>
                            <mainClass>主函数路径</mainClass>
                        </manifest>
                    </archive>
                </configuration>
                <executions>
                    <execution>
                        <id>make-assembly</id>
                        <!-- bind to the packaging phase -->
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```