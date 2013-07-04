[Setup]
AppName=Simple Computer Remote
AppVerName=Simple Computer Remote Server 1.2
AppPublisher=Rekap Logic
AppPublisherURL=http://rekaplogic.com/
AppSupportURL=http://rekaplogic.com/
AppUpdatesURL=http://rekaplogic.com/
DefaultDirName={pf}\SimpleComputerRemote
DefaultGroupName=Simple Computer Remote
AllowNoIcons=yes
OutputBaseFilename=simplecomputerremote_1.2_win64
Compression=lzma
SolidCompression=yes
PrivilegesRequired=admin
ArchitecturesInstallIn64BitMode=x64
ArchitecturesAllowed=x64

[Files]
Source: "..\..\bin\server-windows-amd64.exe"; DestDir: "{app}"; DestName: "RemoteServer.exe"; Flags:ignoreversion
Source: "..\..\remote-icon.ico"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
Name: "{commonstartup}\Simple Computer Remote"; Filename: "{app}\RemoteServer.exe"; IconFilename: "{app}\remote-icon.ico"
Name: "{group}\Simple Computer Remote"; Filename: "{app}\RemoteServer.exe"; IconFilename: "{app}\remote-icon.ico"

[Run]
Filename: "{app}\RemoteServer.exe"; Description: "{cm:LaunchProgram,Simple Computer Remote}"; Flags: nowait postinstall skipifsilent