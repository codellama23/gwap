class TerminaGraph {

	Vertex centerVertex;
	float cx;
	float cy;
	String term;
	ArrayList vertices;

	String newTerm;

	String type;

	TerminaGraph() {

		cx = width/2;
		cy = height/2;

		type = "TerminaGraph";
		term = "term";
		newTerm = "";
		centerVertex = new Vertex(cx, cy, 20, term, 0, GREY);
		centerVertex.display();
		vertices = new ArrayList();
		
	}

	void setTerm(String term) {
		this.term = term;    
		centerVertex = new Vertex(cx, cy, 20, term, 0, GREY);
	}


	void collide() {
		if (vertices.size() > 1) {
			for(Vertex v : vertices){
				for(Vertex w : vertices){
					float w1 = v.tw / 2;
					float w2 = w.tw / 2;
					float h1 = v.th / 2;
					float h2 = w.th / 2;
					float dx = abs(v.x - w.x);
					float dy = abs(v.y - w.y);
					if ( (w1 + w2) > dx && (h1 + h2) > dy) {
						if(w.angle < v.angle){
							w.newAngle -= 0.02;
							v.newAngle += 0.02;
						} else {
							w.newAngle += 0.02;
							v.newAngle -= 0.02;
						}
					}
				}
			}
		}
	}


	void mix() {
		for (int i = 0; i < vertices.size(); i++) {
			int j = (int)random(0, vertices.size());
			Vertex v = vertices.get(i);
			Vertex w = vertices.get(j);
			vertices.set(j, v);
			vertices.set(i, w);
			v.setMovement(j, vertices.size());
			w.setMovement(i, vertices.size());
		}
	}

	Vertex newVertex(String s, float distance, int size, String matchType) {
		Vertex vert = new Vertex(cx, cy - distance, size, s, distance, new color(0));
		if (matchType == "directMatch") {
			vert.c = CORRECT;
		} 
		else if (matchType == "indirectMatch") {
			vert.c = UNKNOWN;
		} 
		else {
			vert.c = WRONG;
		}
		return vert;
	}

	void addTag(String s, float distance, int size, String matchType){
		Vertex vert = newVertex(s,distance,size,matchType);
		vertices.add(0, vert);
		updatePositions();
	}

	void updatePositions() {
		int n = vertices.size();
		if (n < 7) { //small amount
			for (int i = 1; i < n; i++) {

				int to = i;
				int off = 7;

				Vertex v = vertices.get(i);
				v.setMovement(to, off);
			}
		} 
		else {
			for (int i = 1; i < n; i++) {
				int to = i;
				int off = n + 1;
				Vertex v = vertices.get(i);
				v.setMovement(to, off);
			}
		}
	}

	void setDisplayWidth(int newWidth) {
		this.width = newWidth;
		//  externals.canvas.width = width;
		size(newWidth, height);
		cx = (int) newWidth / 2;
		centerVertex.x = cx;
		for (Vertex v : vertices)
			v.move();

		draw();
	}


}
