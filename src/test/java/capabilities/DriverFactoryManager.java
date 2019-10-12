package capabilities;

import java.net.MalformedURLException;
import java.net.URL;


import capabilities.AndroidDevicesCapabilities;
import capabilities.IOSDevicesCapabilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.remote.UnreachableBrowserException;

public class DriverFactoryManager {

    private static AppiumDriver<?> appiumDriver;
    private static AppiumDriverLocalService service;


    public static AppiumDriver<?> startDriverByMavenParameter(String mavenEnvironment) {

        if (appiumDriver == null) {

            try {

                if (mavenEnvironment.contains("ANDROID")) {
                    appiumDriver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), AndroidDevicesCapabilities.valueOf(mavenEnvironment).getAndroidCapabilitiesFromPlataform());
                } else if (mavenEnvironment.contains("IPHONE")) {
                    appiumDriver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), IOSDevicesCapabilities.valueOf(mavenEnvironment).getIOSCapabilitiesFromPlataform());
                }

            } catch (IllegalArgumentException e) {
                System.out.println(" ==== AVISO : Por favor selecionar um dos devices abaixo para executar os testes ==== ");
                AndroidDevicesCapabilities.showAvaliableAndroidDevices();
                IOSDevicesCapabilities.showAvaliableIphoneDevices();
                System.exit(1);
            } catch (UnreachableBrowserException e) {
                System.out.println(" ==== AVISO : Please start appium server, use appium in command line. ====");
                System.exit(1);
            } catch (MalformedURLException e) {
                System.out.println(" ==== AVISO : Por favor verifique a url que foi informada para executar os testes. ====");
                System.exit(1);
            }
        }

        return appiumDriver;
    }

    public static AppiumDriver<?> getDriver() {
        return appiumDriver;
    }

    public static void reLaunchApp() {
        if (appiumDriver != null) {
            appiumDriver.launchApp();
        }
    }

    public static void quitDriver() {
        if (appiumDriver != null) {
            appiumDriver.quit();
        }
    }

    public static String getPageHierarchy() {
        return appiumDriver.getPageSource();
    }

    public static void appiumServerUp() {
        service = AppiumDriverLocalService.buildDefaultService();
        service.start();
    }


    public static void appiumServerDown() {
        service = AppiumDriverLocalService.buildDefaultService();
        service.stop();
    }
}

