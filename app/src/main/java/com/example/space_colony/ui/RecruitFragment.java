package com.example.space_colony.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.space_colony.R;
import com.example.space_colony.logic.Storage;
import com.example.space_colony.model.CrewMember;
import com.example.space_colony.model.Engineer;
import com.example.space_colony.model.Medic;
import com.example.space_colony.model.Pilot;
import com.example.space_colony.model.Scientist;
import com.example.space_colony.model.Soldier;

public class RecruitFragment extends Fragment {
    private EditText nameInput;
    private RadioGroup specializationGroup;
    private TextView statsPreview;

    private static final int[] SKILLS =       {5, 6, 7, 8, 9};
    private static final int[] RESILIENCES =  {4, 3, 2, 1, 0};
    private static final int[] MAX_ENERGIES = {20, 19, 18, 17, 16};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recruit, container, false);

        nameInput = view.findViewById(R.id.name_input);
        specializationGroup = view.findViewById(R.id.specialization_group);
        statsPreview = view.findViewById(R.id.stats_preview);

        specializationGroup.setOnCheckedChangeListener((group, checkedId) -> updateStats(checkedId));

        view.findViewById(R.id.btn_confirm).setOnClickListener(v -> recruit());
        view.findViewById(R.id.btn_cancel).setOnClickListener(v ->
            requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }

    private void updateStats(int checkedId) {
        int index = getSpecIndex(checkedId);
        if (index >= 0) {
            statsPreview.setText("Skill: " + SKILLS[index]
                + "  Resilience: " + RESILIENCES[index]
                + "  Max Energy: " + MAX_ENERGIES[index]);
        }
    }

    private int getSpecIndex(int radioId) {
        if (radioId == R.id.radio_pilot)     return 0;
        if (radioId == R.id.radio_engineer)  return 1;
        if (radioId == R.id.radio_medic)     return 2;
        if (radioId == R.id.radio_scientist) return 3;
        if (radioId == R.id.radio_soldier)   return 4;
        return -1;
    }

    private void recruit() {
        String name = nameInput.getText().toString().trim();
        if (name.isEmpty()) {
            nameInput.setError("Name required");
            return;
        }
        int checkedId = specializationGroup.getCheckedRadioButtonId();
        if (checkedId == -1) {
            Toast.makeText(requireContext(), "Select a specialization", Toast.LENGTH_SHORT).show();
            return;
        }
        CrewMember member = createMember(getSpecIndex(checkedId), name);
        Storage.getInstance().add(member);
        Toast.makeText(requireContext(), name + " recruited!", Toast.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private CrewMember createMember(int index, String name) {
        switch (index) {
            case 1:  return new Engineer(name);
            case 2:  return new Medic(name);
            case 3:  return new Scientist(name);
            case 4:  return new Soldier(name);
            default: return new Pilot(name);
        }
    }
}
