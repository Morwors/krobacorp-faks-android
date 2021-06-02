package com.example.krobachat.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.krobachat.R;
import com.example.krobachat.interfaces.ILoadmore;
import com.example.krobachat.model.User;

import java.util.List;

class LoadingViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public LoadingViewHolder(View view) {
        super(view);
        progressBar = view.findViewById(R.id.progressBarUser);
    }
}

class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name;
    UsersAdapter.OnUserClickListener onUserClickListener;
//    OnNoteListiner
    public UserViewHolder(View view, UsersAdapter.OnUserClickListener onUserClickListener) {
        super(view);
        name = view.findViewById(R.id.txtName);
        this.onUserClickListener = onUserClickListener;

        view.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        onUserClickListener.onUserClick(getAdapterPosition());
    }
}

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnUserClickListener onUserClickListener;
    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    ILoadmore loadmore;
    boolean isLoading;
    Activity activity;
    List<User> users;
    int visibleThreshold = 5;
    int lastVisibleItem, totalItemCount;
    public interface OnUserClickListener{
        void onUserClick(int position);
    }

    public UsersAdapter(RecyclerView recyclerView, Activity activity, List<User> users, OnUserClickListener onUserClickListener) {
        this.activity = activity;
        this.users = users;
        this.onUserClickListener = onUserClickListener;

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadmore != null) {
                        loadmore.onLoadMore();
                    }
                    isLoading = true;
                }
            }

        });

    }

    @Override
    public int getItemViewType(int position) {
        return users.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadmore(ILoadmore loadmore) {
        this.loadmore = loadmore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(activity).inflate(R.layout.text_row_item, parent, false);
            return new UserViewHolder(view, onUserClickListener);
        }else if(viewType == VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(activity).inflate(R.layout.user_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){

        if(holder instanceof UserViewHolder){
            User user = users.get(position);
            UserViewHolder viewHolder = (UserViewHolder) holder;
            viewHolder.name.setText(users.get(position).getUsername());
        }else if(holder instanceof LoadingViewHolder){
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }
    @Override
    public int getItemCount(){
        return users.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

}