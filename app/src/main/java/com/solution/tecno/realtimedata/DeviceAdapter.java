package com.solution.tecno.realtimedata;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    List<DataSnapshot> l;
    private Context c;
    private AlertDialog alertDialog;

    public DeviceAdapter(List<DataSnapshot> l) {
        this.l=l;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        c=parent.getContext();

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Using DataSnapshot
        final DataSnapshot ds=l.get(position);
        final HashMap<String,Object> devices=(HashMap<String,Object>)ds.getValue();
        final String dev_name=(String)devices.get("name");
        final String key=ds.getKey();
        holder.name.setText(dev_name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                alertDialogBuilder.setTitle("Dispositivo");
                alertDialogBuilder.setMessage("Usted ha elegido: "+dev_name);
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)c).deleteElement(key);
            }
        });

        holder.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(c);
                View promptsView = li.inflate(R.layout.edit_item_dialog, null);


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                alertDialogBuilder.setTitle("Dispositivo");
                alertDialogBuilder.setView(promptsView);
                final EditText edit_dev_name=promptsView.findViewById(R.id.edit_device_name);
                edit_dev_name.setText(dev_name);
                alertDialogBuilder.setMessage("Usted ha elegido: "+dev_name);
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)c).updateElement(key,edit_dev_name.getText().toString());
                        alertDialog.dismiss();
                    }
                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        if(l==null){
            return 0;
        }else {
            return l.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        Button btn_delete,btn_update;

        private ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.device_name);
            btn_delete=itemView.findViewById(R.id.btn_delete);
            btn_update=itemView.findViewById(R.id.btn_update);
        }
    }
}
