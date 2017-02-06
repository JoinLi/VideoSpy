package ly.com.videospy.viewholder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import ly.com.videospy.R;
import ly.com.videospy.bean.InforBean;
import ly.com.videospy.bean.MovieBean;


/**
 * Created by Mr.Jude on 2015/2/22.
 */
public class MovieviewHolder extends BaseViewHolder<MovieBean> {
    private TextView mTv_name;
    private ImageView mImg_face;
    private TextView mTv_sign;


    public MovieviewHolder(ViewGroup parent) {
        super(parent, R.layout.item_person);
        mTv_name = $(R.id.person_name);
        mTv_sign = $(R.id.person_sign);
        mImg_face = $(R.id.person_face);

    }

    @Override
    public void setData(final MovieBean person) {
        mTv_name.setText(person.getMovie_title());
        mTv_sign.setText(person.getMovie_api());
//        Glide.with(getContext())
//                .load(person.getImg_url())
//                .placeholder(R.mipmap.ic_launcher)
//                .bitmapTransform(new CropCircleTransformation(getContext()))
//                .into(mImg_face);
    }


}
