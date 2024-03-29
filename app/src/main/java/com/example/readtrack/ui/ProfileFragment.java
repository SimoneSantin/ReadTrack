package com.example.readtrack.ui;

import static com.example.readtrack.util.Constants.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME;
import static com.example.readtrack.util.Constants.FAVOURITES_BOOKS;
import static com.example.readtrack.util.Constants.ID_TOKEN;
import static com.example.readtrack.util.Constants.READING_BOOKS;
import static com.example.readtrack.util.Constants.FINISHED_BOOKS;
import static com.example.readtrack.util.Constants.WANT_TO_READ;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.readtrack.R;
import com.example.readtrack.adapter.BooksRecyclerViewProfile;
import com.example.readtrack.databinding.FragmentProfileBinding;
import com.example.readtrack.model.Book;
import com.example.readtrack.model.Result;
import com.example.readtrack.repository.books.BooksRepository;
import com.example.readtrack.repository.user.IUserRepository;
import com.example.readtrack.ui.welcome.UserViewModel;
import com.example.readtrack.ui.welcome.UserViewModelFactory;
import com.example.readtrack.util.DataEncryptionUtil;
import com.example.readtrack.util.ServiceLocator;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private BooksViewModel booksViewModel;
    private UserViewModel userViewModel;
    private DataEncryptionUtil dataEncryptionUtil;
    private String idToken;
    private List<Book> bookList;

    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BooksRepository booksRepositoryWithLiveData =
                ServiceLocator.getInstance().getBookRepository(requireActivity().getApplication());

        booksViewModel = new ViewModelProvider(
                requireActivity(),
                new BooksViewModelFactory(booksRepositoryWithLiveData)).get(BooksViewModel.class);

        dataEncryptionUtil = new DataEncryptionUtil(requireActivity().getApplication());
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(getActivity().getApplication());
        userViewModel = new ViewModelProvider(
                this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        try {
            idToken = dataEncryptionUtil.readSecretDataWithEncryptedSharedPreferences(
                    ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, ID_TOKEN);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState){
        binding.buttonSettings.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_profile_fragment_to_settingsFragment);
            ((MainActivity) requireActivity()).hideBottomNavigation();
        });
        userViewModel.getUserImage(idToken).observe(
                getViewLifecycleOwner(), result -> {
                    if (result.isSuccess()) {
                        Picasso.get()
                                .load(((Result.UserResponseSuccess) result).getData().getImageLink())
                                .error(R.drawable.image_not_found)
                                .into(binding.profileImageView);
                    } else {
                    }
                }
        );
        binding.userName.setText(userViewModel.getLoggedUser().getEmail());
        generateRecyclerView(view, binding.favBooks, FAVOURITES_BOOKS);
        generateRecyclerView(view, binding.readingBooks, READING_BOOKS);
        generateRecyclerView(view, binding.startBooks, WANT_TO_READ);
        generateRecyclerView(view, binding.booksFinished, FINISHED_BOOKS);
    }
    private void generateRecyclerView(View view, RecyclerView recyclerViewBooks, String path){
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        this.bookList = new ArrayList<>();
        BooksRecyclerViewProfile booksRecyclerViewAdapter = new BooksRecyclerViewProfile(bookList,
                new BooksRecyclerViewProfile.OnItemClickListener(){
                    @Override
                    public void onBooksItemClick(Book books) {
                        booksViewModel.getBooksById(books.getId()).observe(getViewLifecycleOwner(), res -> {
                            if (res.isSuccess()) {
                                ((MainActivity) requireActivity()).hideBottomNavigation();
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("bookArgument", ((Result.BooksResponseSuccess) res).getData().getItems().get(0));
                                Navigation.findNavController(view).navigate(R.id.action_profile_fragment_to_bookFragment, bundle);
                            } else {
                                Log.d("search", "Nessun risultato trovato");
                            }
                        });
                    }

                    @Override
                    public void onButtonItemClick() {
                        Navigation.findNavController(view).popBackStack();
                        Navigation.findNavController(view).navigate(R.id.search_fragment);
                    }
                });
        recyclerViewBooks.setLayoutManager(layoutManager);
        recyclerViewBooks.setAdapter(booksRecyclerViewAdapter);
        switch (path) {
            case FAVOURITES_BOOKS:
                booksViewModel.getFavBooksMutableLiveData(idToken).observe(
                        getViewLifecycleOwner(), result -> {
                            if (result.isSuccess()) {
                                bookList.clear();
                                bookList = ((Result.BooksResponseSuccess) result).getDataBooks().getItems();
                                booksRecyclerViewAdapter.setBookList(bookList);
                                booksRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                            }
                        }
                );
                break;
            case READING_BOOKS:
                booksViewModel.getReadingBooksMutableLiveData(idToken, isConnected()).observe(
                        getViewLifecycleOwner(), result -> {
                            if (result.isSuccess()) {
                                bookList.clear();
                                bookList = ((Result.BooksResponseSuccess) result).getDataBooks().getItems();
                                booksRecyclerViewAdapter.setBookList(bookList);
                                booksRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                            }
                        }
                );
                break;

            case WANT_TO_READ:
                booksViewModel.getSavedBooksMutableLiveData(idToken).observe(
                        getViewLifecycleOwner(), result -> {
                            if (result.isSuccess()) {
                                bookList.clear();
                                bookList = ((Result.BooksResponseSuccess) result).getDataBooks().getItems();
                                booksRecyclerViewAdapter.setBookList(bookList);
                                booksRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                            }
                        }
                );
                break;

            case FINISHED_BOOKS:
                booksViewModel.getFinishedBooksMutableLiveData(idToken).observe(
                        getViewLifecycleOwner(), result -> {
                            if (result.isSuccess()) {
                                bookList.clear();
                                bookList = ((Result.BooksResponseSuccess) result).getDataBooks().getItems();
                                booksRecyclerViewAdapter.setBookList(bookList);
                                booksRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                            }
                        }
                );
                break;

            default:
                Log.d("errore", "path non valido");
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).showBottomNavigation();
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}