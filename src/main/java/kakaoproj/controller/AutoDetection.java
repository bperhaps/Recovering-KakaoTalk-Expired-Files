package kakaoproj.controller;

import be.derycke.pieter.com.COMException;
import jmtp.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class AutoDetection {

    public class ContentRoot {
        final PortableDeviceObject[] root;
        final PortableDevice device;

        public ContentRoot(PortableDeviceObject[] root, PortableDevice device) {
            this.root = root;
            this.device = device;
        }
    }

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

    public ContentRoot getContentRoot() {
        PortableDeviceManager manager = new PortableDeviceManager();
        PortableDevice[] devices = manager.getDevices();

        for(PortableDevice device : devices) {
            try {
                device.open();
                PortableDeviceObject[] root = findContentRoot(device, device.getRootObjects(), "start");
                if (root != null){
                    return new ContentRoot(root, device);
                }
            } finally {
                device.close();
            }
        }

        return null;
    }

    public Path copyFileFromDevice(ContentRoot cr, Path toSrc){
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("kakaoExpired");
            System.out.println(tempDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        copyFileFromDevice(cr.root, cr.device, tempDir, toSrc);
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

    private void copyFileFromDevice(PortableDeviceObject[] pdos, PortableDevice device, Path tmpSrc, Path toSrc) {
        for(PortableDeviceObject pdo : pdos) {
            if(pdo instanceof PortableDeviceFolderObject) {
                copyFileFromDevice(((PortableDeviceFolderObject) pdo).getChildObjects(), device, tmpSrc, toSrc);
            } else {
                try {
                    copy.copyFromPortableDeviceToHost(pdo.getID(), tmpSrc.toString(), device);
                    File tmpFile = new File(tmpSrc.toString() + "/" + pdo.getName());
                    FileHandler.fileTransform(tmpFile, toSrc.toString());
                    tmpFile.delete();
                } catch (COMException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private PortableDeviceToHostImpl32 copy = new PortableDeviceToHostImpl32();
    private HashMap<String, String> path = new HashMap<>();
}
