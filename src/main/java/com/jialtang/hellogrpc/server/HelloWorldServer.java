package com.jialtang.hellogrpc.server;

import com.jialtang.hellogrpc.helloworld.GreeterGrpc;
import com.jialtang.hellogrpc.helloworld.HelloReply;
import com.jialtang.hellogrpc.helloworld.HelloRequest;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HelloWorldServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        HelloWorldServer server = new HelloWorldServer();
        server.start();
        server.blockUntilShutdown();
    }

    final int PORT = 50051;

    private Server server;

    private void start() throws IOException {
        server = ServerBuilder.forPort(PORT).addService(new GreeterImpl()).build().start();
        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread() {
                            @Override
                            public void run() {
                                // Use stderr here since the logger may have been reset by its JVM
                                // shutdown hook.
                                System.err.println(
                                        "*** shutting down gRPC server since JVM is shutting down");
                                try {
                                    HelloWorldServer.this.stop();
                                } catch (InterruptedException e) {
                                    e.printStackTrace(System.err);
                                }
                                System.err.println("*** server shut down");
                            }
                        });
    }

    /** Await termination on the main thread since the grpc library uses daemon threads. */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    static class GreeterImpl extends GreeterGrpc.GreeterImplBase {
        @Override
        public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
            HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
            System.out.println("=====server=====");
            System.out.println("server: Hello " + req.getName());
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }
}
