package cf.aditya.shoppingapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import cf.aditya.shoppingapp.R;
import cf.aditya.shoppingapp.data.Product;

/**
 * Created by HP on 22-12-2017.
 */

public class ProductAdapter extends ArrayAdapter<Product> {

    ArrayList<Product> products;
    int resource;
    Context context;
    LayoutInflater li;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;


    public ProductAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Product> objects) {
        super(context, resource, objects);

        products = objects;
        this.resource =resource;
        this.context = context;

        li = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context).build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(configuration);

        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        viewHolder holder;

        if (convertView==null)
        {
            convertView = li.inflate(resource,null);
            holder = new viewHolder();

            holder.productName = (TextView)convertView.findViewById(R.id.product_Name);
            holder.productImage = (ImageView) convertView.findViewById(R.id.productImage);
            holder.productBrand = (TextView) convertView.findViewById(R.id.productBrand);
            holder.productPrice = (TextView) convertView.findViewById(R.id.product_Price);
            holder.productDiscountedPrice =(TextView) convertView.findViewById(R.id.productDiscountedPrice);


            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }

        holder.productName.setText(products.get(position).getName());
        holder.productPrice.setText(Html.fromHtml("₹ <del>"+products.get(position).getPrice()+"</del>"));
        holder.productDiscountedPrice.setText(Html.fromHtml("₹ "+products.get(position).getDiscountedprice()));
        holder.productBrand.setText(products.get(position).getBrand());
        imageLoader.displayImage(products.get(position).getImage(),holder.productImage);
        return convertView;
    }
   static class viewHolder{
        public ImageView productImage;
        public TextView productName;
        public TextView productBrand;
        public TextView productPrice;
        public TextView productDiscountedPrice;
    }

}


