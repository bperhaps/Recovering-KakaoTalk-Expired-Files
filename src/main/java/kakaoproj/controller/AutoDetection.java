package kakaoproj.controller;

import be.derycke.pieter.com.COMException;
import jmtp.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class AutoDetection {

    private static AutoDetection instance = null;

    public static AutoDetection getInstance() {
        if( instance == null ) {
            instance = new AutoDetection();
        }
        return instance;
    }
    private AutoDetection() {
        //-Djava.library.path="lib path"

        path.put("start", "Phone");
        path.put("Phone", "Android");
        path.put("Android", "data");
        path.put("data", "com.kakao.talk");
        path.put("com.kakao.talk", "contents");
    }

    public Path start() {
        PortableDeviceManager manager = new PortableDeviceManager();
        PortableDevice[] devices = manager.getDevices();
        Path tempDir = null;
        for(PortableDevice device : devices) {
            try {
                device.open();
                PortableDeviceObject[] root = findContentRoot(device, device.getRootObjects(), "start");
                if (root != null){
                    tempDir = Files.createTempDirectory("kakaoExpired");
                    copyFileFromDevice(root, device, tempDir);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                device.close();
            }
        }

        return tempDir;
    }

    private PortableDeviceObject[] findContentRoot(PortableDevice device, PortableDeviceObject[] object, String p) {
        for(PortableDeviceObject o : object) {
            if(o instanceof PortableDeviceStorageObject) {
                PortableDeviceStorageObject pso = (PortableDeviceStorageObject) o;
                PortableDeviceObject[] pdos = findContentRoot(device, pso.getChildObjects(), pso.getName());
                if( pdos == null ) continue;
                else return pdos;
            }

            if(o instanceof PortableDeviceFolderObject){
                PortableDeviceFolderObject pfo = (PortableDeviceFolderObject) o;
                if(path.get(p) != null && path.get(p).equals(pfo.getName())) {
                    if(pfo.getName().equals("contents")){
                        return pfo.getChildObjects();
                    }
                    PortableDeviceObject[] pdos = findContentRoot(device, pfo.getChildObjects(), pfo.getName());
                    if( pdos == null ) continue;
                    else return pdos;
                }
            }
        }
        return null;
    }

    private void copyFileFromDevice(PortableDeviceObject[] pdos, PortableDevice device, Path src) {
        for(PortableDeviceObject pdo : pdos) {
            if(pdo instanceof PortableDeviceFolderObject) {
                copyFileFromDevice(((PortableDeviceFolderObject) pdo).getChildObjects(), device, src);
            } else {
                try {
                    copy.copyFromPortableDeviceToHost(pdo.getID(), src.toString(), device);
                } catch (COMException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private PortableDeviceToHostImpl32 copy = new PortableDeviceToHostImpl32();
    private HashMap<String, String> path = new HashMap<>();
}
