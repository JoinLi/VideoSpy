package ly.com.videospy.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import ly.com.videospy.bean.InforBean;
import ly.com.videospy.bean.MovieBean;
import ly.com.videospy.viewholder.MovieviewHolder;
import ly.com.videospy.viewholder.PersonViewHolder;

/**
 * Created by Mr.Jude on 2015/7/18.
 */
public class MovieAdapter extends RecyclerArrayAdapter<MovieBean> {
    public MovieAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieviewHolder(parent);
    }
}
