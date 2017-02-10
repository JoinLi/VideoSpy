package ly.com.videospy.viewholder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import ly.com.videospy.R;
import ly.com.videospy.bean.MovieBean;


/**
 * Created by Mr.Jude on 2015/2/22.
 */
public class MovieviewHolder extends BaseViewHolder<MovieBean> {
    private TextView text_movie_url;


    public MovieviewHolder(ViewGroup parent) {
        super(parent, R.layout.item_movie);
        text_movie_url = $(R.id.text_movie_url);


    }

    @Override
    public void setData(final MovieBean person) {
        text_movie_url.setText(person.getMovie_title());

    }


}
