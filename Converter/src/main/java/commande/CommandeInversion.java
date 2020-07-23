package commande;

import java.util.Collections;

import affichage.demande.TableVideo;

public class CommandeInversion extends Commande {

	int inv1, inv2;
	
	public CommandeInversion(TableVideo table, int inv1, int inv2) {
		super(table);
		this.inv1 = inv1;
		this.inv2 = inv2;
	}

	@Override
	public boolean execute() {
		try {
			Collections.swap(table.getItems(), inv1, inv2);
			return true;
		}
		catch(ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	@Override
	public boolean annuler() {
		try {
			Collections.swap(table.getItems(), inv2, inv1);
			return true;
		}
		catch(ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	@Override
	public boolean reexecute() {
		try {
			Collections.swap(table.getItems(), inv1, inv2);
			return true;
		}
		catch(ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

}
