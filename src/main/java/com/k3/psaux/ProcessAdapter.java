package com.k3.psaux;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class ProcessAdapter extends RecyclerView.Adapter<ProcessAdapter.ViewHolder> {
    private Context context;
    public ArrayList<CProcess> processes;

    public interface OnItemClickListener {
        void onItemClick(CProcess item, int i);
    }
    private OnItemClickListener onItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txvProcess;
        public ViewHolder(View view) {
            super(view);
            txvProcess = view.findViewById(R.id.txvProcess);
        }
        public void bind(final CProcess item, final int i, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, i);
                }
            });
        }
    }
    public ProcessAdapter(Context context, ArrayList<CProcess> processes, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.processes = processes;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.process_list, parent, false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String user = processes.get(position).getUser();
        String ppid = String.valueOf(processes.get(position).getPPID());
        String pname = "<b><font color=\"" + ContextCompat.getColor(context, R.color.lime) + "\">" + processes.get(position).getPName() + "</font></b>";
        if (!processes.get(position).getCPU().equals("")) {
            pname = "<b><font color=\"" + ContextCompat.getColor(context, R.color.lime) + "\">" + processes.get(position).getPName() + " ~%" + processes.get(position).getCPU() + "</font></b>";
        }
        String pline = user + "  [" + processes.get(position).getPid() + "]  > <small>" + ppid + "</small>  " + pname;
        holder.txvProcess.setText(Html.fromHtml(pline));
        holder.bind(processes.get(position), position, onItemClickListener);
    }
    @Override
    public int getItemCount() {
        return processes.size();
    }

}
