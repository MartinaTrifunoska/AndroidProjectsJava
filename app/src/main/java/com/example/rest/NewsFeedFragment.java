package com.example.rest;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rest.adapters.NewsAdapter;
import com.example.rest.models.Upload;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class NewsFeedFragment extends Fragment {
    RecyclerView recyclerNews;
    NewsAdapter adapter;
    EditText addText;
    Button addImage, addItem;
    ProgressBar progressBar;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    Uri filePath;
    List<Upload> uploads;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_feed, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        if(mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getEmail().equals("martina_trifunoska@hotmail.com")){
            addText.setVisibility(View.VISIBLE);
            addImage.setVisibility(View.VISIBLE);
            addItem.setVisibility(View.VISIBLE);
        }

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = addText.getText().toString();

                if(!text.isEmpty() && filePath != null){
                    progressBar.setVisibility(View.VISIBLE);
                    final StorageReference refImages = storageReference.child("" + System.currentTimeMillis());
                    refImages.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    refImages.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getContext(), "Post uploaded successfully!", Toast.LENGTH_SHORT).show();
                                            Upload upload = new Upload(text, uri.toString());
                                            String uploadID = databaseReference.push().getKey();
                                            databaseReference.child(uploadID).setValue(upload);
                                            addText.setText("");
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Failed upload!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                }else{
                    Toast.makeText(getContext(), "No file selected!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerNews = view.findViewById(R.id.recyclerNews);
        recyclerNews.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Upload upload = dataSnapshot.getValue(Upload.class);
                        uploads.add(0, upload);
                    }
                    adapter = new NewsAdapter(getContext(), uploads);
                    adapter.updateData(uploads);
                    recyclerNews.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init(View view) {
        mAuth = FirebaseAuth.getInstance();

        uploads = new ArrayList<>();

        addText = view.findViewById(R.id.addText);
        addImage = view.findViewById(R.id.addImage);
        addItem = view.findViewById(R.id.addItem);
        progressBar = view.findViewById(R.id.progressBar);

        databaseReference = FirebaseDatabase.getInstance().getReference("news");
        storageReference = FirebaseStorage.getInstance().getReference("news");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
        }
    }
}