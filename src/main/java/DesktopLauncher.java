import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import core.Boot;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		configuration.setForegroundFPS(60);
		configuration.setTitle("Memo Stick Rescue");
		configuration.setMaximized(true);
		configuration.setWindowSizeLimits(1590, 600, 9999, 9999);
		new Lwjgl3Application(new Boot(), configuration);
	}
}
