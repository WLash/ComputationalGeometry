filename = 'polygon.txt';
delimiterIn = ' ';
headerlinesIn = 1;
M = importdata(filename, delimiterIn);
rowCount = size(M,1);

normalValuesMatrix = zeros(rowCount, 3);
dot = zeros;
lines = zeros;



for row= 1:rowCount
  coord1 = M(row,:);
  coord2 = 0;
  if(row == rowCount)
      coord2 = M(1,:);
  else
      coord2 = M(row+1,:);
  end
  
  V = coord2 - coord1;
  
  normal = [-V(2), V(1)];
  nbetrag = norm(normal);
  
  skalarprodukt = coord1(1)*normal(1) + coord1(2) * normal(2);
  
  valueVector = [normal(1), normal(2), nbetrag];
  
  normalValuesMatrix(row,:) = valueVector;
 
  dot(row) = skalarprodukt;
  if(row==1)
    lines = [coord1, coord2];
  else
    lines = [lines, coord1, coord2];
  end
end

normalValuesMatrix
dot

f=[0,0,-1];

circle = linprog(f, normalValuesMatrix, dot)

circleCoords = [circle(1), circle(2)]
M=[M; circleCoords]

viscircles(circleCoords, circle(3))

%fill(M(:,1), M(:,2), 'red')




