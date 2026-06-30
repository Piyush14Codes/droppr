package com.app.droppr.service;

import jakarta.annotation.PreDestroy;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetAddress;

@Component
public class MdnsService {

    private JmDNS jmdns;

    @EventListener(ApplicationReadyEvent.class)
    public void registerService() {
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            jmdns = JmDNS.create(localAddress);

            ServiceInfo serviceInfo = ServiceInfo.create(
                    "_http._tcp.local.",
                    "droppr",
                    8080,
                    "Droppr File Share"
            );

            jmdns.registerService(serviceInfo);
            System.out.println("mDNS service registered: droppr._http._tcp.local.");
        } catch (IOException e) {
            System.err.println("Failed to start mDNS service: " + e.getMessage());
        }
    }

    @PreDestroy
    public void unregisterService() {
        if(jmdns != null) {
            try {
                jmdns.close();
                System.out.println("mDNS service unregistered");
            } catch (IOException e) {
                System.err.println("Error closing mDNS service: " + e.getMessage());
            }
        }
    }
}
