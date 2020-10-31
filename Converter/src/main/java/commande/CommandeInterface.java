package commande;

public interface CommandeInterface {

	public abstract boolean executer();
	public abstract boolean annuler();
	public abstract boolean reexecuter();
}
