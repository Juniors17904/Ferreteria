// Generated by view binder compiler. Do not edit!
package com.example.ferreteria.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.ferreteria.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class NavHeaderMenuBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnIniciarSesion;

  @NonNull
  public final TextView claridad;

  @NonNull
  public final TextView textView4;

  @NonNull
  public final TextView txUsuariologeado;

  private NavHeaderMenuBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnIniciarSesion,
      @NonNull TextView claridad, @NonNull TextView textView4, @NonNull TextView txUsuariologeado) {
    this.rootView = rootView;
    this.btnIniciarSesion = btnIniciarSesion;
    this.claridad = claridad;
    this.textView4 = textView4;
    this.txUsuariologeado = txUsuariologeado;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static NavHeaderMenuBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static NavHeaderMenuBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.nav_header_menu, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static NavHeaderMenuBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnIniciarSesion;
      Button btnIniciarSesion = ViewBindings.findChildViewById(rootView, id);
      if (btnIniciarSesion == null) {
        break missingId;
      }

      id = R.id.claridad;
      TextView claridad = ViewBindings.findChildViewById(rootView, id);
      if (claridad == null) {
        break missingId;
      }

      id = R.id.textView4;
      TextView textView4 = ViewBindings.findChildViewById(rootView, id);
      if (textView4 == null) {
        break missingId;
      }

      id = R.id.txUsuariologeado;
      TextView txUsuariologeado = ViewBindings.findChildViewById(rootView, id);
      if (txUsuariologeado == null) {
        break missingId;
      }

      return new NavHeaderMenuBinding((ConstraintLayout) rootView, btnIniciarSesion, claridad,
          textView4, txUsuariologeado);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
