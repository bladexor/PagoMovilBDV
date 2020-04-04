B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=9.801
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Type contacto_frecuente(banco As String, _
							nombre_contacto As String, _
							prefijo As String, _
							numcelular As String, _
							numcedula As String, _
							monto As String)

	Dim contactos As List
	Dim registro As contacto_frecuente
End Sub

Sub Globals


	Private lvPagosFrecuentes As ListView
	Private btnAddPagoFrecuente As Button
	
	
	'Controles para agregar y editar pagos
	Private btnSave As Button
	Private spBanco As Spinner
	Private spCelular As Spinner
	Private txtCelular As EditText
	Private spCedula As Spinner
	Private txtCedula As EditText
	
	Dim sm As SlidingMenu
	Private txtNombre As EditText
	
	Dim selectedIndex As Int
	
	Dim blnModoEdicion As Boolean=False
End Sub

Sub Activity_Create(FirstTime As Boolean)
	
	
	
	Activity.LoadLayout("pagosfrecuentes")
	
	
	lvPagosFrecuentes.SingleLineLayout.Label.TextColor=Colors.Black
	lvPagosFrecuentes.SingleLineLayout.Label.TextSize=13
	lvPagosFrecuentes.SingleLineLayout.Label.Typeface=Typeface.FONTAWESOME
		
	lvPagosFrecuentes.TwoLinesLayout.Label.TextColor=Colors.Black
	lvPagosFrecuentes.TwoLinesLayout.Label.TextSize=20
	lvPagosFrecuentes.TwoLinesLayout.Label.Typeface=Typeface.DEFAULT
	'lvPagosFrecuentes.TwoLinesLayout.Label.Padding=Array As Int (50dip, 10dip, 10dip, 10dip)
	'lvPagosFrecuentes.TwoLinesLayout.Label.Height=100dip
	lvPagosFrecuentes.TwoLinesLayout.ItemHeight=150
	lvPagosFrecuentes.TwoLinesLayout.Label.Left=25
	lvPagosFrecuentes.TwoLinesLayout.Label.Top=15 
		
	'lvPagosFrecuentes.TwoLinesLayout.secondLabel.TextColor=Colors.RGB(0,100,0)
	lvPagosFrecuentes.TwoLinesLayout.secondLabel.TextSize=14
	lvPagosFrecuentes.TwoLinesLayout.secondLabel.Left=25
	lvPagosFrecuentes.TwoLinesLayout.secondLabel.Top=67
	lvPagosFrecuentes.TwoLinesLayout.secondLabel.Height=80
	'lvPagosFrecuentes.TwoLinesLayout.Label.paren
	'lvPagosFrecuentes.TwoLinesLayout.SecondLabel.
	'lvPagosFrecuentes.TwoLinesLayout.secondLabel.Padding=Array As Int (50dip, 50dip, 10dip, 10dip)
	
	
	If sm.IsInitialized = False Then
		sm.Initialize("sm")
		sm.Menu.LoadLayout("addpagofrecuente")
		'sm.secondaryMenu.LoadLayout("smWksn2")
		sm.BehindOffset = 0%x
		
		sm.Menu.Enabled=False
		sm.Mode = sm.RIGHT
		
		spBanco.DropdownBackgroundColor=Colors.White

		For Each b As Map In Main.Bancos
			spBanco.Add(b.GetKeyAt(0))
		Next
		
		spCelular.DropdownBackgroundColor=Colors.White
		spCelular.AddAll(Array As String("PREFIJO","0412","0414","0416","0424","0426"))
	
		spCedula.DropdownBackgroundColor=Colors.White
		spCedula.Add("V")
		'lblFullName.Text=Main.userFullName
		'lblEmail.Text=Main.userEmail

	End If
	registro.Initialize
	
	
	
	' Tomar la información del archivo y meterlo en la Lista
	contactos.Initialize
	Dim Ra As RandomAccessFile
	
	If File.Exists(File.DirDefaultExternal,"pagosfrecuentes.txt") Then

		Ra.Initialize(File.DirDefaultExternal,"pagosfrecuentes.txt",True)
			Do While Ra.CurrentPosition < Ra.Size
				registro=Ra.ReadObject(Ra.CurrentPosition)
				contactos.Add(registro)
			Loop
			Ra.Close
	End If
	
End Sub

Sub Activity_Resume
	FillPagosFrecuentesList
End Sub



Sub FillPagosFrecuentesList()
	'clvWorkstations.Clear

	lvPagosFrecuentes.Clear
		
	
	If sm.SecondaryVisible=False Then
'		ResetSecondMenu
		
		For Each w As contacto_frecuente In contactos
			'clvWorkstations.Add(CreateListItem(w, clvWorkstations.AsView.Width, 74dip), w)
			
			
			lvPagosFrecuentes.AddTwoLines2(w.nombre_contacto,w.banco.ToUpperCase &  CRLF & w.prefijo & " - " & w.numcelular & "  |  V - " & w.numcedula,w)
				
'			If(sm.SecondaryVisible) Then
'				If(WksnSelected.Get(1)=w.Get(1)) Then	imgWksn.Bitmap=LoadBitmap(File.DirAssets,strImageFile)
'			End If
				
		Next
			
	End If
End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub


Sub btnAddPagoFrecuente_Click
	ResetFormAddPago
	
	Activity.Title="Nuevo Pago Frecuente"
	
	sm.ShowMenu
End Sub

Sub lvPagosFrecuentes_ItemClick (Position As Int, Value As Object)
	
	
	Main.contactoaux.Initialize
	Main.contactoaux=Value
	
	Activity.Finish

End Sub

Sub lvPagosFrecuentes_ItemLongClick (Position As Int, Value As Object)
	selectedIndex=Position
	registro=Value
	Dim l1 As Label
	Dim r1,r2 As RadioButton
	Dim p As Panel
	
	l1.Initialize("")
	l1.TextSize=16
	l1.Text=registro.banco.ToUpperCase &  CRLF & registro.prefijo & " - " & registro.numcelular & "  |  V - " & registro.numcedula
	
	r1.Initialize("")
	r1.Text=Chr(0xf044) & " Editar"
	r1.Typeface=Typeface.FONTAWESOME
	r1.TextSize=16
	r1.TextColor=Colors.Black
	r1.Checked=True
	
	r2.Initialize("")
	r2.Text=Chr(0xf1f8) & " Eliminar"
	r2.Typeface=Typeface.FONTAWESOME
	r2.TextSize=16
	r2.TextColor=Colors.Red
	
	p.Initialize("Panel")		
	p.AddView(l1,30,5,80%x,60)
	p.AddView(r1,10,70,80%x,70)
	p.AddView(r2,10,150,80%x,70)
	
	Dim d1 As CustomDialog
	d1.AddView(p,10,10,100%x,30%y)
	'd1.AddView(b2,10,150,120,70)
	
	Dim result As Int
	
	result=d1.Show(registro.nombre_contacto ,"Confirmar","Cancelar","",Null)
	
		If result=DialogResponse.POSITIVE Then
			
			If(r1.Checked) Then 
				Log("Editar")
				blnModoEdicion=True
				
				Activity.Title="Editar Pago Frecuente"
				txtCedula.Text=registro.numcedula
				txtCelular.Text=registro.numcelular
				spBanco.SelectedIndex=spBanco.IndexOf(registro.banco)
				spCelular.SelectedIndex=spCelular.IndexOf(registro.prefijo)
				txtNombre.Text=registro.nombre_contacto
				sm.ShowMenu
			End If
			
			If(r2.Checked) Then
				Log("Eliminar")
				contactos.RemoveAt(selectedIndex)
				WriteContactsToFile

				FillPagosFrecuentesList
				ToastMessageShow("Pago Frecuente Eliminado con Exito!",True)
			End If
			
			
		End If
	
End Sub

Public Sub ShowMensaje(Mensaje As String)
	Msgbox(Mensaje,"Informacion")
End Sub

Sub btnSave_Click
	If spBanco.SelectedIndex=0 Then
		ShowMensaje("Seleccione banco receptor")
	Else if spCelular.SelectedIndex=0 Then
		ShowMensaje("Seleccione prefijo del celular")
	else If txtCelular.Text.Length <>7 Then
		ShowMensaje("Numero Celular invalido")
	else If txtCedula.Text.Length <>8 Then
		ShowMensaje("Numero de Cedula invalido")
	else If txtNombre.Text.Length=0 Then
		ShowMensaje("Nombre invalido")
	Else
		
		Dim registro As contacto_frecuente
		registro.banco=spBanco.SelectedItem
		registro.nombre_contacto=txtNombre.Text
		registro.prefijo=spCelular.SelectedItem
		registro.numcelular=txtCelular.text
		registro.numcedula=txtCedula.text
		registro.monto=""
		
		If blnModoEdicion Then
			contactos.Set(selectedIndex,registro)
			blnModoEdicion=False
		Else
			contactos.Add(registro)
		End If
		
		WriteContactsToFile
		
	ToastMessageShow("Pago Frecuente Guardado con Exito!",True)
	 
	 sm.HideMenus
	 FillPagosFrecuentesList
	 ResetFormAddPago
	 
	Activity.Title="Pagos Frecuentes"
	End If
End Sub

Sub ResetFormAddPago 
	spBanco.SelectedIndex=0
	spCelular.SelectedIndex=0
	txtNombre.Text=""
	txtCedula.Text=""
	txtCelular.Text=""
End Sub

Sub WriteContactsToFile
	Dim Ra As RandomAccessFile
	If File.Exists(File.DirDefaultExternal,"pagosfrecuentes.txt") Then
		File.Delete(File.DirDefaultExternal,"pagosfrecuentes.txt")
	End If

	Ra.Initialize(File.DirDefaultExternal,"pagosfrecuentes.txt",False)
	For l=0 To contactos.Size-1
		Ra.WriteObject(contactos.Get(l),True,Ra.CurrentPosition)
	Next
	Ra.Close
End Sub

Sub Activity_KeyPress (KeyCode As Int) As Boolean

	If KeyCode = KeyCodes.KEYCODE_BACK Then
		If sm.Visible Then
			sm.HideMenus
			'FillWorkstationList
			Activity.Title="Pagos Frecuentes"
			blnModoEdicion=False
			Return True
		End If
	End If
	Return False
End Sub