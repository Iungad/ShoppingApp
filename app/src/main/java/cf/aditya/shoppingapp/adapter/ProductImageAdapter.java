package cf.aditya.shoppingapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import cf.aditya.shoppingapp.R;

/**
 * Created by HP on 28-12-2017.
 */

public class ProductImageAdapter extends PagerAdapter {
    String[] strings;
    int resource;
    Context context;
    LayoutInflater li;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;



    public ProductImageAdapter(Context context,int resource,String[] strings) {
        this.context = context;
        this.resource = resource;
        this.strings = strings;

        li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context).build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(configuration);

        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==((LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = li.inflate(resource,container,false);

        ImageView imageView =(ImageView)view.findViewById(R.id.productimages);

        imageLoader.displayImage(strings[position],imageView);


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        LinearLayout view = (LinearLayout) object;
        //container.removeView(view);
    }
}
