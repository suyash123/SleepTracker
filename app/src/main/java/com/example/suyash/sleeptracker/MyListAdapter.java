package com.example.suyash.sleeptracker;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.suyash.sleeptracker.entities.DataManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MyViewHolder>{

    private ArrayList<String> myList;
    private Context context;

    public MyListAdapter(ArrayList<String> myList, Context context) {
        this.myList = myList;
        this.context = context;
        loadDataAsync();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.log_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int pos) {
        String log = myList.get(pos);
        myViewHolder.logView.setText(log);
    }

    @Override
    public int getItemCount() {
        return myList == null ? 0 : myList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView logView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            logView = itemView.findViewById(R.id.logView);
        }
    }

    private void loadDataAsync() {
        FetchLogsTask fetchLogsTask = new FetchLogsTask(this);
        fetchLogsTask.execute();
    }

    public class FetchLogsTask extends AsyncTask<Void, Void, ArrayList<String>> {

        WeakReference<MyListAdapter> weakReference;

        public FetchLogsTask(MyListAdapter myListAdapter) {
            weakReference = new WeakReference<>(myListAdapter);
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return new DataManager(weakReference.get().context).getSleepLogs();
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);

            weakReference.get().myList = strings;
            weakReference.get().notifyDataSetChanged();
        }
    }
}
