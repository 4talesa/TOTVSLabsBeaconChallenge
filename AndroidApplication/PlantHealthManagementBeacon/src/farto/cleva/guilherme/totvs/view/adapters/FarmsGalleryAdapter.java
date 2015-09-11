package farto.cleva.guilherme.totvs.view.adapters;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import farto.cleva.guilherme.totvs.vo.FarmVO;

public class FarmsGalleryAdapter extends BaseAdapter {

	private Context context = null;
	private List<FarmVO> farms = null;

	public FarmsGalleryAdapter(Context context, List<FarmVO> farms) {
		super();
		this.context = context;
		this.farms = farms;
	}

	@Override
	public int getCount() {
		return this.farms.size();
	}

	@Override
	public Object getItem(int position) {
		return this.farms.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(this.context);

		imageView.setImageDrawable(this.context.getResources().getDrawable(this.context.getResources().getIdentifier(this.farms.get(position).getThumbnailid(), "drawable", this.context.getPackageName())));
		imageView.setLayoutParams(new Gallery.LayoutParams(215, 215));

		imageView.setScaleType(ImageView.ScaleType.FIT_XY);

		return imageView;
	}

}
