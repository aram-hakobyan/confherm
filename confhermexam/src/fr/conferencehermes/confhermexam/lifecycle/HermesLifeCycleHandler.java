package fr.conferencehermes.confhermexam.lifecycle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

@SuppressLint("NewApi")
public class HermesLifeCycleHandler implements ActivityLifecycleCallbacks {
  // I use four separate variables here. You can, of course, just use two and
  // increment/decrement them instead of using four and incrementing them all.
  private int resumed;
  private int paused;
  private int started;
  private int stopped;

  @Override
  public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

  @Override
  public void onActivityDestroyed(Activity activity) {}

  @Override
  public void onActivityResumed(Activity activity) {
    ++resumed;
  }

  @Override
  public void onActivityPaused(Activity activity) {
    ++paused;
  }

  @Override
  public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

  @Override
  public void onActivityStarted(Activity activity) {
    ++started;
  }

  @Override
  public void onActivityStopped(Activity activity) {
    ++stopped;
  }

  // If you want a static function you can use to check if your application is
  // foreground/background, you can use the following:
  /*
   * // Replace the four variables above with these four private static int resumed; private static
   * int paused; private static int started; private static int stopped;
   * 
   * // And these two public static functions public static boolean isApplicationVisible() { return
   * started > stopped; }
   * 
   * public static boolean isApplicationInForeground() { return resumed > stopped; }
   */
}
