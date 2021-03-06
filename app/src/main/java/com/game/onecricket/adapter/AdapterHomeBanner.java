package com.game.onecricket.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.game.onecricket.activity.VideoViewActivity;
import com.joooonho.SelectableRoundedImageView;
import com.game.onecricket.APICallingPackage.Config;
import com.game.onecricket.Bean.BeanBanner;
import com.game.onecricket.R;

import java.util.List;

public class AdapterHomeBanner extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<BeanBanner> mListenerList;

    public AdapterHomeBanner(List<BeanBanner> mListenerList, Context context) {
        this.context = context;
        this.mListenerList = mListenerList;
    }

    @Override
    public int getCount() {
        return mListenerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_banner, null);

        SelectableRoundedImageView imageView = (SelectableRoundedImageView)view.findViewById(R.id.im_banner);

        String Imagename = mListenerList.get(position).getBanner_image();
        final String Type = mListenerList.get(position).getType();

        Glide.with(context).asBitmap().load(Config.BANNERIMAGE+Imagename)


                                            .apply(new RequestOptions()

                                            .diskCacheStrategy(DiskCacheStrategy.ALL))

                .into(imageView);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(Imagename.contains("cricket.jpg")){

                    System.out.println("banner touched can do something here! "+Imagename);
                }

                String link = mListenerList.get(position).getLink();
                if (link != null && link.trim().length() > 0) {
                    Intent intent = new Intent(context, VideoViewActivity.class);
                    intent.putExtra("Video_Url", link);
                    context.startActivity(intent);
                }

            }
        });

        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
