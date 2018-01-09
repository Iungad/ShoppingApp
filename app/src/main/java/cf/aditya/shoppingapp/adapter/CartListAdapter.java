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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import cf.aditya.shoppingapp.CartActivity;
import cf.aditya.shoppingapp.R;
import cf.aditya.shoppingapp.data.CartItem;
import cf.aditya.shoppingapp.data.Product;

/**
 * Created by HP on 03-01-2018.
 */

public class CartListAdapter extends ArrayAdapter<CartItem> {

    ArrayList<CartItem> cartItems;
    int resource;
    Context context;
    LayoutInflater li;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    CartActivity cat;


    public CartListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<CartItem> objects,CartActivity cat) {
        super(context, resource, objects);

        cartItems = objects;
        this.resource =resource;
        this.context = context;
        this.cat = cat;
        li = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context).build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(configuration);

        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent){
        CartListAdapter.viewHolder holder;

        if (convertView==null)
        {
            convertView = li.inflate(resource,null);
            holder = new CartListAdapter.viewHolder();
            holder.productImage = (ImageView)convertView.findViewById(R.id.cart_list_productImage);
            holder.productName =(TextView)convertView.findViewById(R.id.cart_list_product_Name);
            holder.productQuality = (TextView)convertView.findViewById(R.id.cart_list_qty);
            holder.removeButton = (Button)convertView.findViewById(R.id.cart_list_remove_button);
            holder.productDiscountedPrice = (TextView)convertView.findViewById(R.id.cart_list_productDiscountedPrice);

            convertView.setTag(holder);
        } else {
            holder = (CartListAdapter.viewHolder) convertView.getTag();
        }

        holder.productName.setText(cartItems.get(position).getName());
        holder.productDiscountedPrice.setText("₹"+cartItems.get(position).getDiscountedprice());
        holder.productQuality.setText("Quantity : "+cartItems.get(position).getQuantity());
        holder.removeButton.setText("Remove Item");
        imageLoader.displayImage(cartItems.get(position).getImage(),holder.productImage);

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               cat.deleteItemCart(cartItems.get(position).getId());

            }
        });

        return convertView;
    }
    static class viewHolder{
        public ImageView productImage;
        public TextView productName;
        public Button removeButton;
        public TextView productQuality;
        public TextView productDiscountedPrice;
    }

}
