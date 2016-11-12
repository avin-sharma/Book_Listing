package com.avinsharma.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Avin on 11-11-2016.
 */
public class BookAdapter extends ArrayAdapter<Book>{

    public BookAdapter(Context context, ArrayList<Book> objects){
        super(context,0,objects);
    }

    static class ViewHolder{
        TextView title;
        TextView author;
        TextView description;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.author = (TextView) convertView.findViewById(R.id.author);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Book currentBook = getItem(position);

        viewHolder.author.setText(currentBook.getAuthor());
        viewHolder.title.setText(currentBook.getTitle());
        viewHolder.description.setText(currentBook.getDescription());
        return convertView;
    }
}
