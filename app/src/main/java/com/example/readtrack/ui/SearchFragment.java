package com.example.readtrack.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.readtrack.R;
import com.example.readtrack.model.Book;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.example.readtrack.model.BookViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.example.readtrack.adapter.BooksSearchRecyclerAdapter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;


public class SearchFragment extends Fragment {

    private SearchView searchView;
    private SearchBar searchBar;
    private BooksSearchRecyclerAdapter booksSearchRecyclerViewAdapter;
    private BookViewModel bookViewModel;

    public SearchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.search_view);
        searchBar = view.findViewById(R.id.search_bar);
        searchView
                .getEditText()
                .setOnEditorActionListener(
                        (v, actionId, event) -> {
                            searchBar.setText(searchView.getText());
                            searchView.hide();

                            RecyclerView recyclerViewFavBooks = view.findViewById(R.id.search_results);
                            RecyclerView.LayoutManager layoutManager =
                                    new LinearLayoutManager(requireContext(),
                                            LinearLayoutManager.VERTICAL, false);
                            Log.d("inserimentoBar",searchBar.getText().toString());
                            bookViewModel=new BookViewModel();
                            bookViewModel.searchBooks(searchBar.getText().toString(), "inhautor");
                            searchBar.setText("");
                            // Osserva i risultati della ricerca
                            bookViewModel.getSearchResults().observe(getViewLifecycleOwner(), books -> {
                                if (books != null && !books.isEmpty()) {
                                    Log.d("search result", books.get(0).getVolumeInfo().getTitle());
                                    Log.d("search result", String.valueOf(books.size()));
                                    booksSearchRecyclerViewAdapter = new BooksSearchRecyclerAdapter(books,
                                            requireActivity().getApplication(),
                                            new BooksSearchRecyclerAdapter.OnItemClickListener() {
                                                @Override
                                                public void onBooksItemClick(Book book) {
                                                    Log.d("onBookItemClick","book");
                                                    //Navigation.findNavController(view).navigate(R.id.action_search_fragment_to_bookFragment);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putParcelable("bookArgument", book);
                                                    Navigation.findNavController(view).navigate(R.id.action_search_fragment_to_bookFragment, bundle);
                                                }
                                            });
                                    recyclerViewFavBooks.setLayoutManager(layoutManager);
                                    recyclerViewFavBooks.setAdapter(booksSearchRecyclerViewAdapter);
                                } else {
                                    // Gestisci il caso in cui non ci sono risultati
                                    Log.d("search result", "Nessun risultato trovato");
                                }
                            });
                            return false;
                        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
