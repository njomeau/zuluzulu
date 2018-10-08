package ch.epfl.sweng.zuluzulu;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.View.AssociationCard;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AssociationCardTest {

    private AssociationCard card;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void createCard(){
        DocumentReference ref = FirebaseFirestore.getInstance().collection("assos_info").document("asso1");
        card = new AssociationCard(InstrumentationRegistry.getTargetContext(), ref);
        while(!card.hasLoaded()){}
    }

    @Test
    public void cardNameIsRight(){
        TextView name = card.findViewById(R.id.card_asso_name);
        assertEquals("Agepoly", name.getText());
    }

    @Test
    public void cardDescIsRight(){
        TextView short_desc = card.findViewById(R.id.card_asso_short_desc);
        assertEquals("Representing all students at EPFL", short_desc.getText());
    }


}
