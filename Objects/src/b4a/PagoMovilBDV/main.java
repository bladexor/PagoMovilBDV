package b4a.PagoMovilBDV;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.PagoMovilBDV", "b4a.PagoMovilBDV.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "b4a.PagoMovilBDV", "b4a.PagoMovilBDV.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.PagoMovilBDV.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.obejcts.TTS _tts = null;
public static anywheresoftware.b4a.objects.collections.List _bancos = null;
public static b4a.PagoMovilBDV.pagosfrecuentes._contacto_frecuente _contactoaux = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnsendsms = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spbanco = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spcelular = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtcelular = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spcedula = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtcedula = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtmonto = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblbanco = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcelular = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcedula = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblmonto = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnconfirmpago = null;
public b4a.PagoMovilBDV.pagosfrecuentes _pagosfrecuentes = null;
public b4a.PagoMovilBDV.starter _starter = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (pagosfrecuentes.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.collections.Map _b = null;
 //BA.debugLineNum = 46;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 49;BA.debugLine="StartService(Starter)";
anywheresoftware.b4a.keywords.Common.StartService(processBA,(Object)(mostCurrent._starter.getObject()));
 //BA.debugLineNum = 50;BA.debugLine="Activity.LoadLayout(\"home\")";
mostCurrent._activity.LoadLayout("home",mostCurrent.activityBA);
 //BA.debugLineNum = 51;BA.debugLine="TTS.Initialize(\"tts\")";
_tts.Initialize(processBA,"tts");
 //BA.debugLineNum = 53;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 54;BA.debugLine="contactoAux.Initialize";
_contactoaux.Initialize();
 //BA.debugLineNum = 55;BA.debugLine="Bancos.Initialize()";
_bancos.Initialize();
 //BA.debugLineNum = 57;BA.debugLine="Bancos.Add(CreateMap(\"--SELECCIONAR--\":\"0000\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("--SELECCIONAR--"),(Object)("0000")}).getObject()));
 //BA.debugLineNum = 58;BA.debugLine="Bancos.Add(CreateMap(\"Banco de Venezuela\":\"0102\"";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco de Venezuela"),(Object)("0102")}).getObject()));
 //BA.debugLineNum = 59;BA.debugLine="Bancos.Add(CreateMap(\"Banco Venezolano de Crédit";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco Venezolano de Crédito"),(Object)("0104")}).getObject()));
 //BA.debugLineNum = 60;BA.debugLine="Bancos.Add(CreateMap(\"Banco Mercantil\":\"0105\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco Mercantil"),(Object)("0105")}).getObject()));
 //BA.debugLineNum = 61;BA.debugLine="Bancos.Add(CreateMap(\"Banco Provincial\":\"0108\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco Provincial"),(Object)("0108")}).getObject()));
 //BA.debugLineNum = 62;BA.debugLine="Bancos.Add(CreateMap(\"Bancaribe\":\"0114\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Bancaribe"),(Object)("0114")}).getObject()));
 //BA.debugLineNum = 63;BA.debugLine="Bancos.Add(CreateMap(\"Banco Exterior\":\"0115\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco Exterior"),(Object)("0115")}).getObject()));
 //BA.debugLineNum = 64;BA.debugLine="Bancos.Add(CreateMap(\"Banco Occidental de Descue";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco Occidental de Descuento"),(Object)("0116")}).getObject()));
 //BA.debugLineNum = 65;BA.debugLine="Bancos.Add(CreateMap(\"Banco Caroní\":\"0128\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco Caroní"),(Object)("0128")}).getObject()));
 //BA.debugLineNum = 66;BA.debugLine="Bancos.Add(CreateMap(\"Banesco\":\"0134\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banesco"),(Object)("0134")}).getObject()));
 //BA.debugLineNum = 67;BA.debugLine="Bancos.Add(CreateMap(\"Banco Sofitasa\":\"0137\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco Sofitasa"),(Object)("0137")}).getObject()));
 //BA.debugLineNum = 68;BA.debugLine="Bancos.Add(CreateMap(\"Banco Plaza\":\"0138\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco Plaza"),(Object)("0138")}).getObject()));
 //BA.debugLineNum = 69;BA.debugLine="Bancos.Add(CreateMap(\"Bangente\":\"0146\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Bangente"),(Object)("0146")}).getObject()));
 //BA.debugLineNum = 70;BA.debugLine="Bancos.Add(CreateMap(\"Banco Fondo Común\":\"0151\")";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco Fondo Común"),(Object)("0151")}).getObject()));
 //BA.debugLineNum = 71;BA.debugLine="Bancos.Add(CreateMap(\"100% Banco\":\"0156\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("100% Banco"),(Object)("0156")}).getObject()));
 //BA.debugLineNum = 72;BA.debugLine="Bancos.Add(CreateMap(\"Banco del Sur\":\"0157\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco del Sur"),(Object)("0157")}).getObject()));
 //BA.debugLineNum = 73;BA.debugLine="Bancos.Add(CreateMap(\"Banco del Tesoro\":\"0163\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco del Tesoro"),(Object)("0163")}).getObject()));
 //BA.debugLineNum = 74;BA.debugLine="Bancos.Add(CreateMap(\"Banco Agrícola de Venezuel";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco Agrícola de Venezuela"),(Object)("0166")}).getObject()));
 //BA.debugLineNum = 75;BA.debugLine="Bancos.Add(CreateMap(\"Bancrecer\":\"0168\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Bancrecer"),(Object)("0168")}).getObject()));
 //BA.debugLineNum = 76;BA.debugLine="Bancos.Add(CreateMap(\"Mi Banco\":\"0169\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Mi Banco"),(Object)("0169")}).getObject()));
 //BA.debugLineNum = 77;BA.debugLine="Bancos.Add(CreateMap(\"Banco Activo\":\"0171\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco Activo"),(Object)("0171")}).getObject()));
 //BA.debugLineNum = 78;BA.debugLine="Bancos.Add(CreateMap(\"Bancamiga\":\"0172\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Bancamiga"),(Object)("0172")}).getObject()));
 //BA.debugLineNum = 79;BA.debugLine="Bancos.Add(CreateMap(\"Banco Internacional de Des";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco Internacional de Desarrollo"),(Object)("0173")}).getObject()));
 //BA.debugLineNum = 80;BA.debugLine="Bancos.Add(CreateMap(\"Banplus\":\"0174\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banplus"),(Object)("0174")}).getObject()));
 //BA.debugLineNum = 81;BA.debugLine="Bancos.Add(CreateMap(\"Banco Bicentenario\":\"0175\"";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco Bicentenario"),(Object)("0175")}).getObject()));
 //BA.debugLineNum = 82;BA.debugLine="Bancos.Add(CreateMap(\"Banco de la Fuerza Armada";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco de la Fuerza Armada Nacional Bolivariana"),(Object)("0177")}).getObject()));
 //BA.debugLineNum = 83;BA.debugLine="Bancos.Add(CreateMap(\"Citibank\":\"0190\"))";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Citibank"),(Object)("0190")}).getObject()));
 //BA.debugLineNum = 84;BA.debugLine="Bancos.Add(CreateMap(\"Banco Nacional de Crédito\"";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Banco Nacional de Crédito"),(Object)("0191")}).getObject()));
 //BA.debugLineNum = 85;BA.debugLine="Bancos.Add(CreateMap(\"Instituto Municipal de Cré";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("Instituto Municipal de Crédito Popular"),(Object)("0601")}).getObject()));
 //BA.debugLineNum = 86;BA.debugLine="Bancos.Add(CreateMap(\"ITALCAMBIO Casa de Cambio\"";
_bancos.Add((Object)(anywheresoftware.b4a.keywords.Common.createMap(new Object[] {(Object)("ITALCAMBIO Casa de Cambio"),(Object)("0719")}).getObject()));
 };
 //BA.debugLineNum = 89;BA.debugLine="spBanco.DropdownBackgroundColor=Colors.White";
mostCurrent._spbanco.setDropdownBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 91;BA.debugLine="For Each b As Map In Bancos";
_b = new anywheresoftware.b4a.objects.collections.Map();
{
final anywheresoftware.b4a.BA.IterableList group39 = _bancos;
final int groupLen39 = group39.getSize()
;int index39 = 0;
;
for (; index39 < groupLen39;index39++){
_b.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(group39.Get(index39)));
 //BA.debugLineNum = 92;BA.debugLine="spBanco.Add(b.GetKeyAt(0))";
mostCurrent._spbanco.Add(BA.ObjectToString(_b.GetKeyAt((int) (0))));
 }
};
 //BA.debugLineNum = 95;BA.debugLine="spCelular.DropdownBackgroundColor=Colors.White";
mostCurrent._spcelular.setDropdownBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 96;BA.debugLine="spCelular.AddAll(Array As String(\"PREFIJO\",\"0412\"";
mostCurrent._spcelular.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"PREFIJO","0412","0414","0416","0424","0426"}));
 //BA.debugLineNum = 98;BA.debugLine="spCedula.DropdownBackgroundColor=Colors.White";
mostCurrent._spcedula.setDropdownBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 99;BA.debugLine="spCedula.Add(\"V\")";
mostCurrent._spcedula.Add("V");
 //BA.debugLineNum = 111;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 127;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 130;BA.debugLine="contactoAux.banco=spBanco.SelectedItem";
_contactoaux.banco /*String*/  = mostCurrent._spbanco.getSelectedItem();
 //BA.debugLineNum = 131;BA.debugLine="contactoAux.numcedula=txtCedula.Text";
_contactoaux.numcedula /*String*/  = mostCurrent._txtcedula.getText();
 //BA.debugLineNum = 132;BA.debugLine="contactoAux.numcelular=txtCelular.Text";
_contactoaux.numcelular /*String*/  = mostCurrent._txtcelular.getText();
 //BA.debugLineNum = 133;BA.debugLine="contactoAux.prefijo=spCelular.SelectedItem";
_contactoaux.prefijo /*String*/  = mostCurrent._spcelular.getSelectedItem();
 //BA.debugLineNum = 134;BA.debugLine="contactoAux.monto=txtMonto.Text";
_contactoaux.monto /*String*/  = mostCurrent._txtmonto.getText();
 //BA.debugLineNum = 136;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 117;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 119;BA.debugLine="txtCedula.Text=contactoAux.numcedula";
mostCurrent._txtcedula.setText(BA.ObjectToCharSequence(_contactoaux.numcedula /*String*/ ));
 //BA.debugLineNum = 120;BA.debugLine="txtCelular.Text=contactoAux.numcelular";
mostCurrent._txtcelular.setText(BA.ObjectToCharSequence(_contactoaux.numcelular /*String*/ ));
 //BA.debugLineNum = 121;BA.debugLine="spBanco.SelectedIndex=spBanco.IndexOf(contactoAux";
mostCurrent._spbanco.setSelectedIndex(mostCurrent._spbanco.IndexOf(_contactoaux.banco /*String*/ ));
 //BA.debugLineNum = 122;BA.debugLine="spCelular.SelectedIndex=spCelular.IndexOf(contact";
mostCurrent._spcelular.setSelectedIndex(mostCurrent._spcelular.IndexOf(_contactoaux.prefijo /*String*/ ));
 //BA.debugLineNum = 123;BA.debugLine="txtMonto.Text=contactoAux.monto";
mostCurrent._txtmonto.setText(BA.ObjectToCharSequence(_contactoaux.monto /*String*/ ));
 //BA.debugLineNum = 125;BA.debugLine="End Sub";
return "";
}
public static String  _btnconfirmpago_click() throws Exception{
 //BA.debugLineNum = 255;BA.debugLine="Sub btnConfirmPago_Click";
 //BA.debugLineNum = 258;BA.debugLine="Log (Activity.NumberOfViews)";
anywheresoftware.b4a.keywords.Common.LogImpl("8983043",BA.NumberToString(mostCurrent._activity.getNumberOfViews()),0);
 //BA.debugLineNum = 261;BA.debugLine="End Sub";
return "";
}
public static String  _btnpagofrecuente_click() throws Exception{
 //BA.debugLineNum = 251;BA.debugLine="Sub btnPagoFrecuente_Click";
 //BA.debugLineNum = 252;BA.debugLine="StartActivity(PagosFrecuentes)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._pagosfrecuentes.getObject()));
 //BA.debugLineNum = 253;BA.debugLine="End Sub";
return "";
}
public static String  _btnsendsms_click() throws Exception{
anywheresoftware.b4a.phone.Phone.PhoneSms _sms1 = null;
String _smstext = "";
anywheresoftware.b4a.objects.PanelWrapper _p = null;
anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog _d1 = null;
int _result = 0;
String _codigobanco = "";
anywheresoftware.b4a.objects.collections.Map _banco = null;
 //BA.debugLineNum = 139;BA.debugLine="Sub btnSendSMS_Click";
 //BA.debugLineNum = 140;BA.debugLine="Dim sms1 As PhoneSms";
_sms1 = new anywheresoftware.b4a.phone.Phone.PhoneSms();
 //BA.debugLineNum = 141;BA.debugLine="Dim smsText As String";
_smstext = "";
 //BA.debugLineNum = 143;BA.debugLine="If spBanco.SelectedIndex=0 Then";
if (mostCurrent._spbanco.getSelectedIndex()==0) { 
 //BA.debugLineNum = 144;BA.debugLine="ShowMensaje(\"Seleccione banco receptor\")";
_showmensaje("Seleccione banco receptor");
 }else if(mostCurrent._spcelular.getSelectedIndex()==0) { 
 //BA.debugLineNum = 146;BA.debugLine="ShowMensaje(\"Seleccione prefijo del celular\")";
_showmensaje("Seleccione prefijo del celular");
 }else if(mostCurrent._txtcelular.getText().length()!=7) { 
 //BA.debugLineNum = 148;BA.debugLine="ShowMensaje(\"Numero Celular invalido\")";
_showmensaje("Numero Celular invalido");
 }else if(mostCurrent._txtcedula.getText().length()!=8) { 
 //BA.debugLineNum = 150;BA.debugLine="ShowMensaje(\"Numero de Cedula invalido\")";
_showmensaje("Numero de Cedula invalido");
 }else if(mostCurrent._txtmonto.getText().length()==0) { 
 //BA.debugLineNum = 152;BA.debugLine="ShowMensaje(\"Monto invalido\")";
_showmensaje("Monto invalido");
 }else {
 //BA.debugLineNum = 155;BA.debugLine="Dim p As Panel";
_p = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 156;BA.debugLine="Dim d1 As CustomDialog";
_d1 = new anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog();
 //BA.debugLineNum = 158;BA.debugLine="p.Initialize(\"\")";
_p.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 159;BA.debugLine="p.LoadLayout(\"confirmarpago\")";
_p.LoadLayout("confirmarpago",mostCurrent.activityBA);
 //BA.debugLineNum = 161;BA.debugLine="d1.AddView(p,10,0,100%x,70%y)";
_d1.AddView((android.view.View)(_p.getObject()),(int) (10),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (70),mostCurrent.activityBA));
 //BA.debugLineNum = 163;BA.debugLine="lblBanco.Text=spBanco.SelectedItem.ToUpperCase";
mostCurrent._lblbanco.setText(BA.ObjectToCharSequence(mostCurrent._spbanco.getSelectedItem().toUpperCase()));
 //BA.debugLineNum = 164;BA.debugLine="lblCelular.Text=spCelular.SelectedItem & \"-\" & t";
mostCurrent._lblcelular.setText(BA.ObjectToCharSequence(mostCurrent._spcelular.getSelectedItem()+"-"+mostCurrent._txtcelular.getText()));
 //BA.debugLineNum = 165;BA.debugLine="lblCedula.Text=spCedula.SelectedItem & \"-\" & txt";
mostCurrent._lblcedula.setText(BA.ObjectToCharSequence(mostCurrent._spcedula.getSelectedItem()+"-"+mostCurrent._txtcedula.getText()));
 //BA.debugLineNum = 166;BA.debugLine="lblMonto.Text=MyFormat(txtMonto.Text)";
mostCurrent._lblmonto.setText(BA.ObjectToCharSequence(_myformat((double)(Double.parseDouble(mostCurrent._txtmonto.getText())))));
 //BA.debugLineNum = 179;BA.debugLine="Log(File.DirDefaultExternal)";
anywheresoftware.b4a.keywords.Common.LogImpl("8393256",anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),0);
 //BA.debugLineNum = 181;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 183;BA.debugLine="result=d1.Show(\"Confirmar Pago\" ,\"ENVIAR\",\"Cance";
_result = _d1.Show("Confirmar Pago","ENVIAR","Cancelar","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 185;BA.debugLine="If result=DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 187;BA.debugLine="Dim codigoBanco As String";
_codigobanco = "";
 //BA.debugLineNum = 188;BA.debugLine="Dim banco As Map";
_banco = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 190;BA.debugLine="banco=Bancos.Get(spBanco.SelectedIndex)";
_banco.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_bancos.Get(mostCurrent._spbanco.getSelectedIndex())));
 //BA.debugLineNum = 191;BA.debugLine="codigoBanco=banco.Get(spBanco.SelectedItem)";
_codigobanco = BA.ObjectToString(_banco.Get((Object)(mostCurrent._spbanco.getSelectedItem())));
 //BA.debugLineNum = 193;BA.debugLine="smsText=\"Pagar \" & codigoBanco & \" \" & spCelula";
_smstext = "Pagar "+_codigobanco+" "+mostCurrent._spcelular.getSelectedItem()+mostCurrent._txtcelular.getText()+" "+mostCurrent._txtcedula.getText()+" "+mostCurrent._txtmonto.getText()+",00";
 //BA.debugLineNum = 195;BA.debugLine="ToastMessageShow(\"Enviando SMS: \" & CRLF &  sms";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Enviando SMS: "+anywheresoftware.b4a.keywords.Common.CRLF+_smstext),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 196;BA.debugLine="sms1.Send(\"2661\",smsText)";
_sms1.Send("2661",_smstext);
 //BA.debugLineNum = 198;BA.debugLine="ProgressDialogShow2(\"Enviando SMS: \" & CRLF &";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Enviando SMS: "+anywheresoftware.b4a.keywords.Common.CRLF+_smstext),anywheresoftware.b4a.keywords.Common.True);
 };
 };
 //BA.debugLineNum = 207;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 23;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 27;BA.debugLine="Private btnSendSMS As Button";
mostCurrent._btnsendsms = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private spBanco As Spinner";
mostCurrent._spbanco = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private spCelular As Spinner";
mostCurrent._spcelular = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private txtCelular As EditText";
mostCurrent._txtcelular = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private spCedula As Spinner";
mostCurrent._spcedula = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private txtCedula As EditText";
mostCurrent._txtcedula = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private txtMonto As EditText";
mostCurrent._txtmonto = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private lblBanco As Label";
mostCurrent._lblbanco = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private lblCelular As Label";
mostCurrent._lblcelular = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private lblCedula As Label";
mostCurrent._lblcedula = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private lblMonto As Label";
mostCurrent._lblmonto = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private btnConfirmPago As Button";
mostCurrent._btnconfirmpago = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 44;BA.debugLine="End Sub";
return "";
}
public static String  _myformat(double _d) throws Exception{
String _s = "";
 //BA.debugLineNum = 218;BA.debugLine="Sub MyFormat(d As Double) As String";
 //BA.debugLineNum = 219;BA.debugLine="Dim s As String = NumberFormat2(d, 0, 0, 0, True)";
_s = anywheresoftware.b4a.keywords.Common.NumberFormat2(_d,(int) (0),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 220;BA.debugLine="Return s.Replace(\",\", \".\") & \",00\"";
if (true) return _s.replace(",",".")+",00";
 //BA.debugLineNum = 221;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
pagosfrecuentes._process_globals();
starter._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 17;BA.debugLine="Public TTS As TTS";
_tts = new anywheresoftware.b4a.obejcts.TTS();
 //BA.debugLineNum = 19;BA.debugLine="Public Bancos As List";
_bancos = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 20;BA.debugLine="Public contactoAux As contacto_frecuente";
_contactoaux = new b4a.PagoMovilBDV.pagosfrecuentes._contacto_frecuente();
 //BA.debugLineNum = 21;BA.debugLine="End Sub";
return "";
}
public static String  _showmensaje(String _mensaje) throws Exception{
 //BA.debugLineNum = 223;BA.debugLine="Public Sub ShowMensaje(Mensaje As String)";
 //BA.debugLineNum = 224;BA.debugLine="Msgbox(Mensaje,\"Informacion\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence(_mensaje),BA.ObjectToCharSequence("Informacion"),mostCurrent.activityBA);
 //BA.debugLineNum = 225;BA.debugLine="End Sub";
return "";
}
public static String  _spbanco_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 227;BA.debugLine="Sub spBanco_ItemClick (Position As Int, Value As O";
 //BA.debugLineNum = 229;BA.debugLine="End Sub";
return "";
}
public static String  _spcedula_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 231;BA.debugLine="Sub spCedula_ItemClick (Position As Int, Value As";
 //BA.debugLineNum = 233;BA.debugLine="End Sub";
return "";
}
public static String  _spcelular_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 235;BA.debugLine="Sub spCelular_ItemClick (Position As Int, Value As";
 //BA.debugLineNum = 241;BA.debugLine="End Sub";
return "";
}
public static String  _tts_ready(boolean _success) throws Exception{
 //BA.debugLineNum = 113;BA.debugLine="Sub TTS_Ready (Success As Boolean)";
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
return "";
}
public static String  _txtcelular_textchanged(String _old,String _new) throws Exception{
 //BA.debugLineNum = 243;BA.debugLine="Sub txtCelular_TextChanged (Old As String, New As";
 //BA.debugLineNum = 249;BA.debugLine="End Sub";
return "";
}
public static String  _txttelefono_textchanged(String _old,String _new) throws Exception{
 //BA.debugLineNum = 210;BA.debugLine="Sub txtTelefono_TextChanged (Old As String, New As";
 //BA.debugLineNum = 211;BA.debugLine="If New.Length=11 Then";
if (_new.length()==11) { 
 //BA.debugLineNum = 212;BA.debugLine="btnSendSMS.Enabled=True";
mostCurrent._btnsendsms.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 214;BA.debugLine="btnSendSMS.Enabled=False";
mostCurrent._btnsendsms.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 216;BA.debugLine="End Sub";
return "";
}
}
