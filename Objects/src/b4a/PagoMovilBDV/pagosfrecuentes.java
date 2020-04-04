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

public class pagosfrecuentes extends Activity implements B4AActivity{
	public static pagosfrecuentes mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.PagoMovilBDV", "b4a.PagoMovilBDV.pagosfrecuentes");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (pagosfrecuentes).");
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
		activityBA = new BA(this, layout, processBA, "b4a.PagoMovilBDV", "b4a.PagoMovilBDV.pagosfrecuentes");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.PagoMovilBDV.pagosfrecuentes", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (pagosfrecuentes) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (pagosfrecuentes) Resume **");
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
		return pagosfrecuentes.class;
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
        BA.LogInfo("** Activity (pagosfrecuentes) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            pagosfrecuentes mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (pagosfrecuentes) Resume **");
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
public static anywheresoftware.b4a.objects.collections.List _contactos = null;
public static b4a.PagoMovilBDV.pagosfrecuentes._contacto_frecuente _registro = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lvpagosfrecuentes = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnaddpagofrecuente = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnsave = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spbanco = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spcelular = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtcelular = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spcedula = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtcedula = null;
public anywheresoftware.b4a.objects.SlidingMenuWrapper _sm = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtnombre = null;
public static int _selectedindex = 0;
public static boolean _blnmodoedicion = false;
public b4a.PagoMovilBDV.main _main = null;
public b4a.PagoMovilBDV.starter _starter = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static class _contacto_frecuente{
public boolean IsInitialized;
public String banco;
public String nombre_contacto;
public String prefijo;
public String numcelular;
public String numcedula;
public String monto;
public void Initialize() {
IsInitialized = true;
banco = "";
nombre_contacto = "";
prefijo = "";
numcelular = "";
numcedula = "";
monto = "";
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.collections.Map _b = null;
anywheresoftware.b4a.randomaccessfile.RandomAccessFile _ra = null;
 //BA.debugLineNum = 43;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 47;BA.debugLine="Activity.LoadLayout(\"pagosfrecuentes\")";
mostCurrent._activity.LoadLayout("pagosfrecuentes",mostCurrent.activityBA);
 //BA.debugLineNum = 50;BA.debugLine="lvPagosFrecuentes.SingleLineLayout.Label.TextColo";
mostCurrent._lvpagosfrecuentes.getSingleLineLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 51;BA.debugLine="lvPagosFrecuentes.SingleLineLayout.Label.TextSize";
mostCurrent._lvpagosfrecuentes.getSingleLineLayout().Label.setTextSize((float) (13));
 //BA.debugLineNum = 52;BA.debugLine="lvPagosFrecuentes.SingleLineLayout.Label.Typeface";
mostCurrent._lvpagosfrecuentes.getSingleLineLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.getFONTAWESOME());
 //BA.debugLineNum = 54;BA.debugLine="lvPagosFrecuentes.TwoLinesLayout.Label.TextColor=";
mostCurrent._lvpagosfrecuentes.getTwoLinesLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 55;BA.debugLine="lvPagosFrecuentes.TwoLinesLayout.Label.TextSize=2";
mostCurrent._lvpagosfrecuentes.getTwoLinesLayout().Label.setTextSize((float) (20));
 //BA.debugLineNum = 56;BA.debugLine="lvPagosFrecuentes.TwoLinesLayout.Label.Typeface=T";
mostCurrent._lvpagosfrecuentes.getTwoLinesLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 59;BA.debugLine="lvPagosFrecuentes.TwoLinesLayout.ItemHeight=150";
mostCurrent._lvpagosfrecuentes.getTwoLinesLayout().setItemHeight((int) (150));
 //BA.debugLineNum = 60;BA.debugLine="lvPagosFrecuentes.TwoLinesLayout.Label.Left=25";
mostCurrent._lvpagosfrecuentes.getTwoLinesLayout().Label.setLeft((int) (25));
 //BA.debugLineNum = 61;BA.debugLine="lvPagosFrecuentes.TwoLinesLayout.Label.Top=15";
mostCurrent._lvpagosfrecuentes.getTwoLinesLayout().Label.setTop((int) (15));
 //BA.debugLineNum = 64;BA.debugLine="lvPagosFrecuentes.TwoLinesLayout.secondLabel.Text";
mostCurrent._lvpagosfrecuentes.getTwoLinesLayout().SecondLabel.setTextSize((float) (14));
 //BA.debugLineNum = 65;BA.debugLine="lvPagosFrecuentes.TwoLinesLayout.secondLabel.Left";
mostCurrent._lvpagosfrecuentes.getTwoLinesLayout().SecondLabel.setLeft((int) (25));
 //BA.debugLineNum = 66;BA.debugLine="lvPagosFrecuentes.TwoLinesLayout.secondLabel.Top=";
mostCurrent._lvpagosfrecuentes.getTwoLinesLayout().SecondLabel.setTop((int) (67));
 //BA.debugLineNum = 67;BA.debugLine="lvPagosFrecuentes.TwoLinesLayout.secondLabel.Heig";
mostCurrent._lvpagosfrecuentes.getTwoLinesLayout().SecondLabel.setHeight((int) (80));
 //BA.debugLineNum = 73;BA.debugLine="If sm.IsInitialized = False Then";
if (mostCurrent._sm.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 74;BA.debugLine="sm.Initialize(\"sm\")";
mostCurrent._sm.Initialize(mostCurrent.activityBA,"sm");
 //BA.debugLineNum = 75;BA.debugLine="sm.Menu.LoadLayout(\"addpagofrecuente\")";
mostCurrent._sm.getMenu().LoadLayout("addpagofrecuente",mostCurrent.activityBA);
 //BA.debugLineNum = 77;BA.debugLine="sm.BehindOffset = 0%x";
mostCurrent._sm.setBehindOffset(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA));
 //BA.debugLineNum = 79;BA.debugLine="sm.Menu.Enabled=False";
mostCurrent._sm.getMenu().setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 80;BA.debugLine="sm.Mode = sm.RIGHT";
mostCurrent._sm.setMode(mostCurrent._sm.RIGHT);
 //BA.debugLineNum = 82;BA.debugLine="spBanco.DropdownBackgroundColor=Colors.White";
mostCurrent._spbanco.setDropdownBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 84;BA.debugLine="For Each b As Map In Main.Bancos";
_b = new anywheresoftware.b4a.objects.collections.Map();
{
final anywheresoftware.b4a.BA.IterableList group22 = mostCurrent._main._bancos /*anywheresoftware.b4a.objects.collections.List*/ ;
final int groupLen22 = group22.getSize()
;int index22 = 0;
;
for (; index22 < groupLen22;index22++){
_b.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(group22.Get(index22)));
 //BA.debugLineNum = 85;BA.debugLine="spBanco.Add(b.GetKeyAt(0))";
mostCurrent._spbanco.Add(BA.ObjectToString(_b.GetKeyAt((int) (0))));
 }
};
 //BA.debugLineNum = 88;BA.debugLine="spCelular.DropdownBackgroundColor=Colors.White";
mostCurrent._spcelular.setDropdownBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 89;BA.debugLine="spCelular.AddAll(Array As String(\"PREFIJO\",\"0412";
mostCurrent._spcelular.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"PREFIJO","0412","0414","0416","0424","0426"}));
 //BA.debugLineNum = 91;BA.debugLine="spCedula.DropdownBackgroundColor=Colors.White";
mostCurrent._spcedula.setDropdownBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 92;BA.debugLine="spCedula.Add(\"V\")";
mostCurrent._spcedula.Add("V");
 };
 //BA.debugLineNum = 97;BA.debugLine="registro.Initialize";
_registro.Initialize();
 //BA.debugLineNum = 102;BA.debugLine="contactos.Initialize";
_contactos.Initialize();
 //BA.debugLineNum = 103;BA.debugLine="Dim Ra As RandomAccessFile";
_ra = new anywheresoftware.b4a.randomaccessfile.RandomAccessFile();
 //BA.debugLineNum = 105;BA.debugLine="If File.Exists(File.DirDefaultExternal,\"pagosfrec";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"pagosfrecuentes.txt")) { 
 //BA.debugLineNum = 107;BA.debugLine="Ra.Initialize(File.DirDefaultExternal,\"pagosfrec";
_ra.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"pagosfrecuentes.txt",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 108;BA.debugLine="Do While Ra.CurrentPosition < Ra.Size";
while (_ra.CurrentPosition<_ra.getSize()) {
 //BA.debugLineNum = 109;BA.debugLine="registro=Ra.ReadObject(Ra.CurrentPosition)";
_registro = (b4a.PagoMovilBDV.pagosfrecuentes._contacto_frecuente)(_ra.ReadObject(_ra.CurrentPosition));
 //BA.debugLineNum = 110;BA.debugLine="contactos.Add(registro)";
_contactos.Add((Object)(_registro));
 }
;
 //BA.debugLineNum = 112;BA.debugLine="Ra.Close";
_ra.Close();
 };
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 301;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 303;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 304;BA.debugLine="If sm.Visible Then";
if (mostCurrent._sm.getVisible()) { 
 //BA.debugLineNum = 305;BA.debugLine="sm.HideMenus";
mostCurrent._sm.HideMenus();
 //BA.debugLineNum = 307;BA.debugLine="Activity.Title=\"Pagos Frecuentes\"";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence("Pagos Frecuentes"));
 //BA.debugLineNum = 308;BA.debugLine="blnModoEdicion=False";
_blnmodoedicion = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 309;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 //BA.debugLineNum = 312;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 313;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 147;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 149;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 117;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 118;BA.debugLine="FillPagosFrecuentesList";
_fillpagosfrecuenteslist();
 //BA.debugLineNum = 119;BA.debugLine="End Sub";
return "";
}
public static String  _btnaddpagofrecuente_click() throws Exception{
 //BA.debugLineNum = 152;BA.debugLine="Sub btnAddPagoFrecuente_Click";
 //BA.debugLineNum = 153;BA.debugLine="ResetFormAddPago";
_resetformaddpago();
 //BA.debugLineNum = 155;BA.debugLine="Activity.Title=\"Nuevo Pago Frecuente\"";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence("Nuevo Pago Frecuente"));
 //BA.debugLineNum = 157;BA.debugLine="sm.ShowMenu";
mostCurrent._sm.ShowMenu();
 //BA.debugLineNum = 158;BA.debugLine="End Sub";
return "";
}
public static String  _btnsave_click() throws Exception{
 //BA.debugLineNum = 240;BA.debugLine="Sub btnSave_Click";
 //BA.debugLineNum = 241;BA.debugLine="If spBanco.SelectedIndex=0 Then";
if (mostCurrent._spbanco.getSelectedIndex()==0) { 
 //BA.debugLineNum = 242;BA.debugLine="ShowMensaje(\"Seleccione banco receptor\")";
_showmensaje("Seleccione banco receptor");
 }else if(mostCurrent._spcelular.getSelectedIndex()==0) { 
 //BA.debugLineNum = 244;BA.debugLine="ShowMensaje(\"Seleccione prefijo del celular\")";
_showmensaje("Seleccione prefijo del celular");
 }else if(mostCurrent._txtcelular.getText().length()!=7) { 
 //BA.debugLineNum = 246;BA.debugLine="ShowMensaje(\"Numero Celular invalido\")";
_showmensaje("Numero Celular invalido");
 }else if(mostCurrent._txtcedula.getText().length()!=8) { 
 //BA.debugLineNum = 248;BA.debugLine="ShowMensaje(\"Numero de Cedula invalido\")";
_showmensaje("Numero de Cedula invalido");
 }else if(mostCurrent._txtnombre.getText().length()==0) { 
 //BA.debugLineNum = 250;BA.debugLine="ShowMensaje(\"Nombre invalido\")";
_showmensaje("Nombre invalido");
 }else {
 //BA.debugLineNum = 253;BA.debugLine="Dim registro As contacto_frecuente";
_registro = new b4a.PagoMovilBDV.pagosfrecuentes._contacto_frecuente();
 //BA.debugLineNum = 254;BA.debugLine="registro.banco=spBanco.SelectedItem";
_registro.banco /*String*/  = mostCurrent._spbanco.getSelectedItem();
 //BA.debugLineNum = 255;BA.debugLine="registro.nombre_contacto=txtNombre.Text";
_registro.nombre_contacto /*String*/  = mostCurrent._txtnombre.getText();
 //BA.debugLineNum = 256;BA.debugLine="registro.prefijo=spCelular.SelectedItem";
_registro.prefijo /*String*/  = mostCurrent._spcelular.getSelectedItem();
 //BA.debugLineNum = 257;BA.debugLine="registro.numcelular=txtCelular.text";
_registro.numcelular /*String*/  = mostCurrent._txtcelular.getText();
 //BA.debugLineNum = 258;BA.debugLine="registro.numcedula=txtCedula.text";
_registro.numcedula /*String*/  = mostCurrent._txtcedula.getText();
 //BA.debugLineNum = 259;BA.debugLine="registro.monto=\"\"";
_registro.monto /*String*/  = "";
 //BA.debugLineNum = 261;BA.debugLine="If blnModoEdicion Then";
if (_blnmodoedicion) { 
 //BA.debugLineNum = 262;BA.debugLine="contactos.Set(selectedIndex,registro)";
_contactos.Set(_selectedindex,(Object)(_registro));
 //BA.debugLineNum = 263;BA.debugLine="blnModoEdicion=False";
_blnmodoedicion = anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 265;BA.debugLine="contactos.Add(registro)";
_contactos.Add((Object)(_registro));
 };
 //BA.debugLineNum = 268;BA.debugLine="WriteContactsToFile";
_writecontactstofile();
 //BA.debugLineNum = 270;BA.debugLine="ToastMessageShow(\"Pago Frecuente Guardado con Exi";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Pago Frecuente Guardado con Exito!"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 272;BA.debugLine="sm.HideMenus";
mostCurrent._sm.HideMenus();
 //BA.debugLineNum = 273;BA.debugLine="FillPagosFrecuentesList";
_fillpagosfrecuenteslist();
 //BA.debugLineNum = 274;BA.debugLine="ResetFormAddPago";
_resetformaddpago();
 //BA.debugLineNum = 276;BA.debugLine="Activity.Title=\"Pagos Frecuentes\"";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence("Pagos Frecuentes"));
 };
 //BA.debugLineNum = 278;BA.debugLine="End Sub";
return "";
}
public static String  _fillpagosfrecuenteslist() throws Exception{
b4a.PagoMovilBDV.pagosfrecuentes._contacto_frecuente _w = null;
 //BA.debugLineNum = 123;BA.debugLine="Sub FillPagosFrecuentesList()";
 //BA.debugLineNum = 126;BA.debugLine="lvPagosFrecuentes.Clear";
mostCurrent._lvpagosfrecuentes.Clear();
 //BA.debugLineNum = 129;BA.debugLine="If sm.SecondaryVisible=False Then";
if (mostCurrent._sm.getSecondaryVisible()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 132;BA.debugLine="For Each w As contacto_frecuente In contactos";
{
final anywheresoftware.b4a.BA.IterableList group3 = _contactos;
final int groupLen3 = group3.getSize()
;int index3 = 0;
;
for (; index3 < groupLen3;index3++){
_w = (b4a.PagoMovilBDV.pagosfrecuentes._contacto_frecuente)(group3.Get(index3));
 //BA.debugLineNum = 136;BA.debugLine="lvPagosFrecuentes.AddTwoLines2(w.nombre_contact";
mostCurrent._lvpagosfrecuentes.AddTwoLines2(BA.ObjectToCharSequence(_w.nombre_contacto /*String*/ ),BA.ObjectToCharSequence(_w.banco /*String*/ .toUpperCase()+anywheresoftware.b4a.keywords.Common.CRLF+_w.prefijo /*String*/ +" - "+_w.numcelular /*String*/ +"  |  V - "+_w.numcedula /*String*/ ),(Object)(_w));
 }
};
 };
 //BA.debugLineNum = 145;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 20;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 23;BA.debugLine="Private lvPagosFrecuentes As ListView";
mostCurrent._lvpagosfrecuentes = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private btnAddPagoFrecuente As Button";
mostCurrent._btnaddpagofrecuente = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private btnSave As Button";
mostCurrent._btnsave = new anywheresoftware.b4a.objects.ButtonWrapper();
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
 //BA.debugLineNum = 35;BA.debugLine="Dim sm As SlidingMenu";
mostCurrent._sm = new anywheresoftware.b4a.objects.SlidingMenuWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private txtNombre As EditText";
mostCurrent._txtnombre = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Dim selectedIndex As Int";
_selectedindex = 0;
 //BA.debugLineNum = 40;BA.debugLine="Dim blnModoEdicion As Boolean=False";
_blnmodoedicion = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 41;BA.debugLine="End Sub";
return "";
}
public static String  _lvpagosfrecuentes_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 160;BA.debugLine="Sub lvPagosFrecuentes_ItemClick (Position As Int,";
 //BA.debugLineNum = 163;BA.debugLine="Main.contactoaux.Initialize";
mostCurrent._main._contactoaux /*b4a.PagoMovilBDV.pagosfrecuentes._contacto_frecuente*/ .Initialize();
 //BA.debugLineNum = 164;BA.debugLine="Main.contactoaux=Value";
mostCurrent._main._contactoaux /*b4a.PagoMovilBDV.pagosfrecuentes._contacto_frecuente*/  = (b4a.PagoMovilBDV.pagosfrecuentes._contacto_frecuente)(_value);
 //BA.debugLineNum = 166;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 168;BA.debugLine="End Sub";
return "";
}
public static String  _lvpagosfrecuentes_itemlongclick(int _position,Object _value) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _l1 = null;
anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _r1 = null;
anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _r2 = null;
anywheresoftware.b4a.objects.PanelWrapper _p = null;
anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog _d1 = null;
int _result = 0;
 //BA.debugLineNum = 170;BA.debugLine="Sub lvPagosFrecuentes_ItemLongClick (Position As I";
 //BA.debugLineNum = 171;BA.debugLine="selectedIndex=Position";
_selectedindex = _position;
 //BA.debugLineNum = 172;BA.debugLine="registro=Value";
_registro = (b4a.PagoMovilBDV.pagosfrecuentes._contacto_frecuente)(_value);
 //BA.debugLineNum = 173;BA.debugLine="Dim l1 As Label";
_l1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 174;BA.debugLine="Dim r1,r2 As RadioButton";
_r1 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
_r2 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 175;BA.debugLine="Dim p As Panel";
_p = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 177;BA.debugLine="l1.Initialize(\"\")";
_l1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 178;BA.debugLine="l1.TextSize=16";
_l1.setTextSize((float) (16));
 //BA.debugLineNum = 179;BA.debugLine="l1.Text=registro.banco.ToUpperCase &  CRLF & regi";
_l1.setText(BA.ObjectToCharSequence(_registro.banco /*String*/ .toUpperCase()+anywheresoftware.b4a.keywords.Common.CRLF+_registro.prefijo /*String*/ +" - "+_registro.numcelular /*String*/ +"  |  V - "+_registro.numcedula /*String*/ ));
 //BA.debugLineNum = 181;BA.debugLine="r1.Initialize(\"\")";
_r1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 182;BA.debugLine="r1.Text=Chr(0xf044) & \" Editar\"";
_r1.setText(BA.ObjectToCharSequence(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf044)))+" Editar"));
 //BA.debugLineNum = 183;BA.debugLine="r1.Typeface=Typeface.FONTAWESOME";
_r1.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.getFONTAWESOME());
 //BA.debugLineNum = 184;BA.debugLine="r1.TextSize=16";
_r1.setTextSize((float) (16));
 //BA.debugLineNum = 185;BA.debugLine="r1.TextColor=Colors.Black";
_r1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 186;BA.debugLine="r1.Checked=True";
_r1.setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 188;BA.debugLine="r2.Initialize(\"\")";
_r2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 189;BA.debugLine="r2.Text=Chr(0xf1f8) & \" Eliminar\"";
_r2.setText(BA.ObjectToCharSequence(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (0xf1f8)))+" Eliminar"));
 //BA.debugLineNum = 190;BA.debugLine="r2.Typeface=Typeface.FONTAWESOME";
_r2.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.getFONTAWESOME());
 //BA.debugLineNum = 191;BA.debugLine="r2.TextSize=16";
_r2.setTextSize((float) (16));
 //BA.debugLineNum = 192;BA.debugLine="r2.TextColor=Colors.Red";
_r2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 194;BA.debugLine="p.Initialize(\"Panel\")";
_p.Initialize(mostCurrent.activityBA,"Panel");
 //BA.debugLineNum = 195;BA.debugLine="p.AddView(l1,30,5,80%x,60)";
_p.AddView((android.view.View)(_l1.getObject()),(int) (30),(int) (5),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),(int) (60));
 //BA.debugLineNum = 196;BA.debugLine="p.AddView(r1,10,70,80%x,70)";
_p.AddView((android.view.View)(_r1.getObject()),(int) (10),(int) (70),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),(int) (70));
 //BA.debugLineNum = 197;BA.debugLine="p.AddView(r2,10,150,80%x,70)";
_p.AddView((android.view.View)(_r2.getObject()),(int) (10),(int) (150),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),(int) (70));
 //BA.debugLineNum = 199;BA.debugLine="Dim d1 As CustomDialog";
_d1 = new anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog();
 //BA.debugLineNum = 200;BA.debugLine="d1.AddView(p,10,10,100%x,30%y)";
_d1.AddView((android.view.View)(_p.getObject()),(int) (10),(int) (10),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (30),mostCurrent.activityBA));
 //BA.debugLineNum = 203;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 205;BA.debugLine="result=d1.Show(registro.nombre_contacto ,\"Confirm";
_result = _d1.Show(_registro.nombre_contacto /*String*/ ,"Confirmar","Cancelar","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 207;BA.debugLine="If result=DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 209;BA.debugLine="If(r1.Checked) Then";
if ((_r1.getChecked())) { 
 //BA.debugLineNum = 210;BA.debugLine="Log(\"Editar\")";
anywheresoftware.b4a.keywords.Common.LogImpl("81572904","Editar",0);
 //BA.debugLineNum = 211;BA.debugLine="blnModoEdicion=True";
_blnmodoedicion = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 213;BA.debugLine="Activity.Title=\"Editar Pago Frecuente\"";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence("Editar Pago Frecuente"));
 //BA.debugLineNum = 214;BA.debugLine="txtCedula.Text=registro.numcedula";
mostCurrent._txtcedula.setText(BA.ObjectToCharSequence(_registro.numcedula /*String*/ ));
 //BA.debugLineNum = 215;BA.debugLine="txtCelular.Text=registro.numcelular";
mostCurrent._txtcelular.setText(BA.ObjectToCharSequence(_registro.numcelular /*String*/ ));
 //BA.debugLineNum = 216;BA.debugLine="spBanco.SelectedIndex=spBanco.IndexOf(registro";
mostCurrent._spbanco.setSelectedIndex(mostCurrent._spbanco.IndexOf(_registro.banco /*String*/ ));
 //BA.debugLineNum = 217;BA.debugLine="spCelular.SelectedIndex=spCelular.IndexOf(regi";
mostCurrent._spcelular.setSelectedIndex(mostCurrent._spcelular.IndexOf(_registro.prefijo /*String*/ ));
 //BA.debugLineNum = 218;BA.debugLine="txtNombre.Text=registro.nombre_contacto";
mostCurrent._txtnombre.setText(BA.ObjectToCharSequence(_registro.nombre_contacto /*String*/ ));
 //BA.debugLineNum = 219;BA.debugLine="sm.ShowMenu";
mostCurrent._sm.ShowMenu();
 };
 //BA.debugLineNum = 222;BA.debugLine="If(r2.Checked) Then";
if ((_r2.getChecked())) { 
 //BA.debugLineNum = 223;BA.debugLine="Log(\"Eliminar\")";
anywheresoftware.b4a.keywords.Common.LogImpl("81572917","Eliminar",0);
 //BA.debugLineNum = 224;BA.debugLine="contactos.RemoveAt(selectedIndex)";
_contactos.RemoveAt(_selectedindex);
 //BA.debugLineNum = 225;BA.debugLine="WriteContactsToFile";
_writecontactstofile();
 //BA.debugLineNum = 227;BA.debugLine="FillPagosFrecuentesList";
_fillpagosfrecuenteslist();
 //BA.debugLineNum = 228;BA.debugLine="ToastMessageShow(\"Pago Frecuente Eliminado con";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Pago Frecuente Eliminado con Exito!"),anywheresoftware.b4a.keywords.Common.True);
 };
 };
 //BA.debugLineNum = 234;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Type contacto_frecuente(banco As String, _";
;
 //BA.debugLineNum = 16;BA.debugLine="Dim contactos As List";
_contactos = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 17;BA.debugLine="Dim registro As contacto_frecuente";
_registro = new b4a.PagoMovilBDV.pagosfrecuentes._contacto_frecuente();
 //BA.debugLineNum = 18;BA.debugLine="End Sub";
return "";
}
public static String  _resetformaddpago() throws Exception{
 //BA.debugLineNum = 280;BA.debugLine="Sub ResetFormAddPago";
 //BA.debugLineNum = 281;BA.debugLine="spBanco.SelectedIndex=0";
mostCurrent._spbanco.setSelectedIndex((int) (0));
 //BA.debugLineNum = 282;BA.debugLine="spCelular.SelectedIndex=0";
mostCurrent._spcelular.setSelectedIndex((int) (0));
 //BA.debugLineNum = 283;BA.debugLine="txtNombre.Text=\"\"";
mostCurrent._txtnombre.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 284;BA.debugLine="txtCedula.Text=\"\"";
mostCurrent._txtcedula.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 285;BA.debugLine="txtCelular.Text=\"\"";
mostCurrent._txtcelular.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 286;BA.debugLine="End Sub";
return "";
}
public static String  _showmensaje(String _mensaje) throws Exception{
 //BA.debugLineNum = 236;BA.debugLine="Public Sub ShowMensaje(Mensaje As String)";
 //BA.debugLineNum = 237;BA.debugLine="Msgbox(Mensaje,\"Informacion\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence(_mensaje),BA.ObjectToCharSequence("Informacion"),mostCurrent.activityBA);
 //BA.debugLineNum = 238;BA.debugLine="End Sub";
return "";
}
public static String  _writecontactstofile() throws Exception{
anywheresoftware.b4a.randomaccessfile.RandomAccessFile _ra = null;
int _l = 0;
 //BA.debugLineNum = 288;BA.debugLine="Sub WriteContactsToFile";
 //BA.debugLineNum = 289;BA.debugLine="Dim Ra As RandomAccessFile";
_ra = new anywheresoftware.b4a.randomaccessfile.RandomAccessFile();
 //BA.debugLineNum = 290;BA.debugLine="If File.Exists(File.DirDefaultExternal,\"pagosfrec";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"pagosfrecuentes.txt")) { 
 //BA.debugLineNum = 291;BA.debugLine="File.Delete(File.DirDefaultExternal,\"pagosfrecue";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"pagosfrecuentes.txt");
 };
 //BA.debugLineNum = 294;BA.debugLine="Ra.Initialize(File.DirDefaultExternal,\"pagosfrecu";
_ra.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"pagosfrecuentes.txt",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 295;BA.debugLine="For l=0 To contactos.Size-1";
{
final int step6 = 1;
final int limit6 = (int) (_contactos.getSize()-1);
_l = (int) (0) ;
for (;_l <= limit6 ;_l = _l + step6 ) {
 //BA.debugLineNum = 296;BA.debugLine="Ra.WriteObject(contactos.Get(l),True,Ra.CurrentP";
_ra.WriteObject(_contactos.Get(_l),anywheresoftware.b4a.keywords.Common.True,_ra.CurrentPosition);
 }
};
 //BA.debugLineNum = 298;BA.debugLine="Ra.Close";
_ra.Close();
 //BA.debugLineNum = 299;BA.debugLine="End Sub";
return "";
}
}
