package com.example.gifify_challenge.ui.fragments.fragmentGifFavouritesScreen;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.gifify_challenge.R;
import com.example.gifify_challenge.core.entities.GifEntity;
import com.example.gifify_challenge.databinding.FragmentGifFavouritesScreenBinding;
import com.example.gifify_challenge.ui.adapters.AdapterGifFavouritesScreen;
import com.example.gifify_challenge.ui.dialogs.DialogBase;
import com.example.gifify_challenge.utils.SpacingItemDecoration;
import com.example.gifify_challenge.utils.Util;
import com.example.gifify_challenge.viewmodels.ViewmodelGifFavouritesScreen;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;

/*
 * Favourites Gifs Screen
 */
public class FragmentGifFavouritesScreen extends Fragment implements AdapterGifFavouritesScreen.FavouriteListener {

    private AdapterGifFavouritesScreen adapterGifFavouritesScreen;
    private ViewmodelGifFavouritesScreen viewmodelGifFavouritesScreen;
    private FragmentGifFavouritesScreenBinding binding;

    public FragmentGifFavouritesScreen() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.slide_left));
        setEnterTransition(inflater.inflateTransition(R.transition.slide_right));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGifFavouritesScreenBinding.inflate(inflater, container, false);
        View fragmentFavouriteView = binding.getRoot();
        initRecyclerViewFavouriteList();

        return fragmentFavouriteView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeViewmodelData();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewmodelGifFavouritesScreen.getFavouritetList();
    }

    // Observer Pattern
    private void observeViewmodelData() {
        viewmodelGifFavouritesScreen = ViewModelProviders.of(requireActivity()).get(ViewmodelGifFavouritesScreen.class);

        final Observer<List<GifEntity>> observerFavouriteList = new Observer<List<GifEntity>>() {
            @Override
            public void onChanged(List<GifEntity> gifEntities) {
                if (gifEntities != null) {
                    adapterGifFavouritesScreen.loadFavourites(gifEntities);
                }
                binding.imageViewDefaultFavourites.linearDefaultListContainer.setVisibility(View.GONE);
                binding.progressBarFavs.progressBarWait.setVisibility(View.GONE);
                binding.progressBarFavs.overlay.setVisibility(View.GONE);

                if (adapterGifFavouritesScreen.getItemCount() == 0) {
                    binding.imageViewDefaultFavourites.linearDefaultListContainer.setVisibility(View.VISIBLE);
                } else {
                    binding.imageViewDefaultFavourites.linearDefaultListContainer.setVisibility(View.GONE);
                }
            }
        };
        viewmodelGifFavouritesScreen.getFavouriteGifList().observe(requireActivity(), observerFavouriteList);
    }

    private void initRecyclerViewFavouriteList() {
        adapterGifFavouritesScreen = new AdapterGifFavouritesScreen(this);
        binding.recyclerViewGifFavourite.setAdapter(adapterGifFavouritesScreen);
        binding.recyclerViewGifFavourite.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recyclerViewGifFavourite.addItemDecoration(new SpacingItemDecoration(2, Util.dpToPx(getContext(), 3), true));
        binding.recyclerViewGifFavourite.setHasFixedSize(true);
    }

    @Override
    public void deleteFromList(GifEntity gif, int position) {
        DialogBase dialogBase = new DialogBase(
            gif,
            getString(R.string.delete_favourites),
            getString(R.string.delete_upper),
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewmodelGifFavouritesScreen.deleteFavouriteGif(gif.getId());
                    adapterGifFavouritesScreen.removeFromList(position);
                    Util.setActionSnackBar(
                            getView(),
                            getString(R.string.remove_from_favourites),
                            "",
                            Snackbar.LENGTH_LONG,
                            null);
                    if (adapterGifFavouritesScreen.getItemCount() == 0) {
                        binding.imageViewDefaultFavourites.linearDefaultListContainer.setVisibility(View.VISIBLE);
                    } else {
                        binding.imageViewDefaultFavourites.linearDefaultListContainer.setVisibility(View.GONE);
                    }
                }
            }
        );
        dialogBase.show(getChildFragmentManager(), getString(R.string.dialog_favourites));
    }

    @Override
    public void shareGif(GifEntity gif) {
        Util.shareGif(gif, getContext(), getChildFragmentManager());
    }
}