import java.io.*;
import java.util.*;

public class SnakesLadder{
	
	int N, M;
	int snakes[];
	int ladders[];
	int xdis[];
	int ydis[];
	ArrayList<Integer> inversesnake[];
	ArrayList<Integer> inverseladder[];
	Queue<Integer> q2;
	boolean yvisited[];
	
	public SnakesLadder(String name)throws Exception{
		File file = new File(name);
		BufferedReader br = new BufferedReader(new FileReader(file));
		N = Integer.parseInt(br.readLine());
        
        M = Integer.parseInt(br.readLine());

	    snakes = new int[N];
		ladders = new int[N];
	    for (int i = 0; i < N; i++){
			snakes[i] = -1;
			ladders[i] = -1;
		}
		
		for(int i=0;i<M;i++){
            String e = br.readLine();
            StringTokenizer st = new StringTokenizer(e);
            int source = Integer.parseInt(st.nextToken());
            int destination = Integer.parseInt(st.nextToken());

			if(source<destination){
				ladders[source] = destination;
			}
			else{
				snakes[source] = destination;
			}
        }

		yvisited= new boolean[N+1];
		inversesnake= new ArrayList[N+1];
		inverseladder= new ArrayList[N+1];
		for(int i=0;i<N+1;i++){
			inverseladder[i]= new ArrayList<>();
			inversesnake[i]=new ArrayList<>();
		}
		xdis= new int[N+1];
		boolean xvisited[]= new boolean[N+1];
		Queue<Integer> q= new LinkedList<>();
		int count=0;
		q.add(0);
		xvisited[0]=true;
		while(!q.isEmpty()){
			count++;
			int size=q.size();
			while(size>0){
				int s=q.remove();
				for(int i=1;i<7;i++){
					if(s+i==N && xvisited[N]==false){
						xvisited[N]=true;
						xdis[N]=count;
					}
					else if(s+i<N && xvisited[s+i]==false && (ladders[s+i]!=-1 || snakes[s+i]!=-1)){
						int lad=0;
						if(ladders[s+i]!=-1){
							xdis[s+i]=count;
							inversesnake[ladders[s+i]].add(s+i);
							xvisited[s+i]=true;
							lad=ladders[s+i];
						}
						else if(snakes[s+i]!=-1){
							xdis[s+i]=count;
							inverseladder[snakes[s+i]].add(s+i);
							xvisited[s+i]=true;
							lad=snakes[s+i];
						}
						while(lad<N && (ladders[lad]!=-1 || snakes[lad]!=-1) && xvisited[lad]==false){
							if(snakes[lad]!=-1){
								inverseladder[snakes[lad]].add(lad);
								xdis[lad]=count;
								xvisited[lad]=true;
								lad=snakes[lad];
							}
							else if(ladders[lad]!=-1){
								inversesnake[ladders[lad]].add(lad);
								xdis[lad]=count;
								xvisited[lad]=true;
								lad=ladders[lad];
							}
						}
						if(xvisited[lad]==false){
							q.add(lad);
							xdis[lad]=count;
							xvisited[lad]=true;
						}
					}
					else if(s+i<N && xvisited[s+i]==false && ladders[s+i]==-1 && snakes[s+i]==-1){
						xvisited[s+i]=true;
						q.add(s+i);
						xdis[s+i]=count;
					}
				}
				size--;
			}
		}

		ydis= new int[N+1];
		q2= new LinkedList<>();
		int count2=0;
		q2.add(N);
		yvisited[N]=true;
		addqueue(N, count2);
		while(!q2.isEmpty()){
			count2++;
			int size=q2.size();
			while(size>0){
				int s=q2.remove();
				for(int i=1;i<7;i++){
					if(s-i==1 && yvisited[1]==false){
						ydis[1]=count2;
						yvisited[1]=true;
					}
					else if(s-i>1 && ((inverseladder[s-i].size()!=0)||(inversesnake[s-i].size()!=0)) && yvisited[s-i]==false){
						addqueue(s-i,count2);
					}
					else if(s-i>1 && yvisited[s-i]==false && inverseladder[s-i].size()==0 && inversesnake[s-i].size()==0){
						q2.add(s-i);
						ydis[s-i]=count2;
						yvisited[s-i]=true;
					}
				}
				size--;
			}
		}
	}
    
	private void addqueue(int n, int coun){
		if(inverseladder[n].size()==0 && inversesnake[n].size()==0 && yvisited[n]==false){
			q2.add(n);
			ydis[n]=coun;
			yvisited[n]=true;
		}
		else if(inverseladder[n].size()!=0){
			ydis[n]=coun;
			yvisited[n]=true;
			int k=0;
			while(k<inverseladder[n].size()){
				if(yvisited[inverseladder[n].get(k)]==false){
					addqueue(inverseladder[n].get(k),ydis[n]);
				}
				k++;
			}
		}
		if(inversesnake[n].size()!=0){
			if(yvisited[n]==false){
				ydis[n]=coun;
				yvisited[n]=true;
			}
			int k=0;
			while(k<inversesnake[n].size()){
				if(yvisited[inversesnake[n].get(k)]==false){
					addqueue(inversesnake[n].get(k),ydis[n]);
				}
				k++;
			}
		}
	}
    
	public int OptimalMoves(){
		if(xdis[N]!=0){
			return xdis[N];
		}
		return -1;
	}

	public int Query(int x, int y){
		if(ydis[y]+xdis[x]<xdis[N]){
			return +1;
		}
		return -1;
	}

	public int[] FindBestNewSnake(){
		int result[] = {-1, -1};
		if(this.OptimalMoves()==-1){
			return result;
		}
		int minhead=Integer.MAX_VALUE;
		int sum=Integer.MAX_VALUE;
		int head=0,tail=0;
		for(int i=N-1;i>=2;i--){
			if(inversesnake[i].size()!=0){
				if(minhead>xdis[i]){
					minhead=xdis[i];
					head=i;
					if(tail!=0){
						sum=minhead+tail;
					}
				}
				// else if(sum>xdis[i]+tail && tail!=0){
				// 	minhead=xdis[i];
				// 	head=i;
				// 	sum=xdis[i]+tail;
				// }
			}
			else if(ladders[i]!=-1){
				if(tail==0 && ladders[i]!=head){
					tail=i;
					sum=minhead+ydis[i];
				}
				if(sum>ydis[i]+minhead && ladders[i]!=head){
					tail=ydis[i];
					tail=i;
					sum=ydis[i]+minhead;
				}
			}
		}
		if(head!=0 && tail!=0 && this.Query(head, tail)==1){
			result[0]=head;
			result[1]=tail;
		}
		return result;
	}
}