package kafka_consumer_producer;

public class Destination {
	
	int init_x;
	int init_y;
	int fin_x;
	int fin_y;
	
	public Destination(int x1, int y1, int x2, int y2) {
		init_x = x1;
		init_y = y1;
		fin_x = x2;
		fin_y = y2;
	}

	/**
	 * @return the init_x
	 */
	public int getInit_x() {
		return init_x;
	}

	/**
	 * @param init_x the init_x to set
	 */
	public void setInit_x(int init_x) {
		this.init_x = init_x;
	}

	/**
	 * @return the init_y
	 */
	public int getInit_y() {
		return init_y;
	}

	/**
	 * @param init_y the init_y to set
	 */
	public void setInit_y(int init_y) {
		this.init_y = init_y;
	}

	/**
	 * @return the fin_x
	 */
	public int getFin_x() {
		return fin_x;
	}

	/**
	 * @param fin_x the fin_x to set
	 */
	public void setFin_x(int fin_x) {
		this.fin_x = fin_x;
	}

	/**
	 * @return the fin_y
	 */
	public int getFin_y() {
		return fin_y;
	}

	/**
	 * @param fin_y the fin_y to set
	 */
	public void setFin_y(int fin_y) {
		this.fin_y = fin_y;
	}

	
}
