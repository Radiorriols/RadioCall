package org.radiorriols.radiocall;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    private MediaRecorder recorder;
    private TextView status;
    private File output;
    private int source = MediaRecorder.AudioSource.MIC;
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(36, 50, 36, 20);
        TextView title = new TextView(this);
        title.setText("RadioCall · prueba de grabación\nHTC U11 / Android 9"); title.setTextSize(23); layout.addView(title);
        status = new TextView(this); status.setText("Listo. Prueba con permiso de la otra persona.\nLa captura de la voz remota no está garantizada."); status.setPadding(0,30,0,30); layout.addView(status);
        addButton(layout,"Iniciar grabación (micrófono)",v -> { source=MediaRecorder.AudioSource.MIC; start(); });
        addButton(layout,"Iniciar prueba (VOICE_COMMUNICATION)",v -> { source=MediaRecorder.AudioSource.VOICE_COMMUNICATION; start(); });
        addButton(layout,"Abrir marcador para llamar",v -> { startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"))); });
        addButton(layout,"Detener y guardar",v -> stop());
        addButton(layout,"Compartir última grabación",v -> share());
        TextView note = new TextView(this);
        note.setPadding(0,30,0,0);
        note.setText("Consejo: inicia la grabación, abre el marcador y usa el altavoz durante la llamada. Regresa aquí para detenerla. Los archivos se guardan en el almacenamiento privado de la app. No se envía audio a Internet.");
        layout.addView(note); setContentView(layout);
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 100);
    }
    private void addButton(LinearLayout layout, String name, View.OnClickListener action) {
        Button b = new Button(this); b.setText(name); b.setOnClickListener(action); layout.addView(b);
    }
    private void start() {
        if(recorder!=null) {status.setText("Ya está grabando. Detén primero.");return;}
        if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED){requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},100);return;}
        File dir=getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        if(dir==null){status.setText("No se puede acceder al almacenamiento.");return;}
        output=new File(dir,"RadioCall_"+new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.US).format(new Date())+".m4a");
        try {
            recorder=new MediaRecorder(); recorder.setAudioSource(source);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setAudioEncodingBitRate(128000); recorder.setAudioSamplingRate(44100);
            recorder.setOutputFile(output.getAbsolutePath()); recorder.prepare(); recorder.start();
            status.setText("GRABANDO · "+(source==MediaRecorder.AudioSource.MIC?"MIC":"VOICE_COMMUNICATION")+"\n"+output.getName());
        } catch(Exception ex){ cleanup();status.setText("No se ha podido iniciar: "+ex.getMessage()); }
    }
    private void stop() {
        if(recorder==null){status.setText("No hay grabación activa.");return;}
        try { recorder.stop(); status.setText("Guardado: "+output.getAbsolutePath()); }
        catch(RuntimeException ex){if(output!=null)output.delete();status.setText("Grabación fallida o demasiado corta.");}
        finally {cleanup();}
    }
    private void cleanup(){if(recorder!=null){try{recorder.reset();recorder.release();}catch(Exception ignored){}recorder=null;}}
    private void share(){
        if(output==null||!output.exists()){Toast.makeText(this,"No hay archivo guardado",Toast.LENGTH_SHORT).show();return;}
        // Compartir archivos privados requiere FileProvider; se evita exponer file:// URIs.
        status.setText("Archivo guardado en:\n"+output.getAbsolutePath()+"\nConecta el móvil por USB para copiarlo. La función Compartir llegará en otra versión.");
    }
    @Override protected void onDestroy(){super.onDestroy();if(recorder!=null)stop();}
}
