package com.example.drawforme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Holder> {

    private List<PostModel> postModelList = new ArrayList<>();
    private Context context;

    public Adapter() {
        // Default constructor
    }

    public Adapter(ArrayList<PostModel> postModelList) {
        this.postModelList = postModelList;
    }

    // 뷰홀더 생성
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_recycler, parent, false);
        return new Holder(view);
    }

    // 뷰홀더가 재활용될 때 실행되는 method
    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv.setText(postModelList.get(position).getTitle());
        holder.tv2.setText(postModelList.get(position).getAuthor());

        if(postModelList.get(position).getIsExist() == true) {
            DrawableCompat.setTint(holder.iv1.getDrawable(), ContextCompat.getColor(context, R.color.green1));
        }

        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PostActivity.class);
                intent.putExtra("title_", postModelList.get(position).getTitle());
                intent.putExtra("desc_", postModelList.get(position).getDesc());
                intent.putExtra("uuid_", postModelList.get(position).getUuid());
                intent.putExtra("author_", postModelList.get(position).getAuthor());
                v.getContext().startActivity(intent);
            }
        });
    }

    // 아이템 개수 조회
    @Override
    public int getItemCount() {
        return (postModelList != null ? postModelList.size() : 0);
    }
}

class Holder extends RecyclerView.ViewHolder {
    TextView tv; // title
    TextView tv2; // author
    ImageView iv1; // left icons

    public Holder(@NonNull View itemView) {
        super(itemView);
        tv = itemView.findViewById(R.id.show_items_tv);
        tv2 = itemView.findViewById(R.id.show_author_name_tv);
        iv1 = itemView.findViewById(R.id.left_icon);
    }
}