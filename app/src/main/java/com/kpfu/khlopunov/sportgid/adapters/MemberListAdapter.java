package com.kpfu.khlopunov.sportgid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.models.User;

import java.util.Collections;
import java.util.List;

/**
 * Created by hlopu on 16.12.2017.
 */

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MemberViewHolder> {
    private Context context;
    private List<User> mUsers;
    private MemberListener memberListener;

    public MemberListAdapter(Context context, List<User> mUsers) {
        this.context = context;
        this.mUsers = Collections.emptyList();
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.member_item, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemberViewHolder holder, int position) {
        final User user = mUsers.get(position);
        holder.tvName.setText((user.getName()+" "+user.getSurname()));
        holder.itemView.setOnClickListener(v -> {
//            memberListener.onAlarmClick(user);
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMember;
        TextView tvName;

        public MemberViewHolder(View itemView) {
            super(itemView);
            ivMember = itemView.findViewById(R.id.iv_member_photo);
            tvName = itemView.findViewById(R.id.tv_item_member_fio);
        }
    }

    public interface MemberListener {
        void onAlarmClick(User user);
    }

    public void setMemberListener(MemberListener memberListener) {
        this.memberListener = memberListener;
    }

    public List<User> getmUsers() {
        return mUsers;
    }

    public void setmUsers(List<User> mUsers) {
        this.mUsers = mUsers;
    }
}
