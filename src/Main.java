import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] arg) {
    List<Integer> integerList = readFileIntoIntegerList("IntegerArray.txt");
    // List<Integer> integerList = Arrays.asList(1, 3, 5, 2, 4, 6);
    long numInversions = sortAndCountInv(integerList);
    System.out.println("NumInversions: " + numInversions);
  }

  public static long sortAndCountInv(List<Integer> list) {
    return sortAndCountInv(list, new ArrayList<>(list.size()));
  }

  public static long sortAndCountInv(List<Integer> list, List<Integer> sortedList) {
    if (list.size() < 2) {
      sortedList.addAll(list);
      return 0;
    }
    int n = list.size();
    List<Integer> leftList = list.subList(0, n / 2);
    List<Integer> rightList = list.subList(n / 2, n);
    List<Integer> sortedLeftList = new ArrayList<>(leftList.size());
    List<Integer> sortedRightList = new ArrayList<>(rightList.size());
    long leftInv = sortAndCountInv(leftList, sortedLeftList);
    long rightInv = sortAndCountInv(rightList, sortedRightList);
    long splitInv = mergeAndCountSplitInv(sortedLeftList, sortedRightList, sortedList);
    return leftInv + rightInv + splitInv;
  }

  public static long mergeAndCountSplitInv(List<Integer> sortedLeftList,
      List<Integer> sortedRightList, List<Integer> mergedList) {
    final int nLeft = sortedLeftList.size();
    final int nRight = sortedRightList.size();

    long splitInversions = 0;
    int iLeft = 0;
    int iRight = 0;

    while(iLeft < nLeft && iRight < nRight) {
      if (sortedLeftList.get(iLeft) < sortedRightList.get(iRight)) {
        mergedList.add(sortedLeftList.get(iLeft));
        iLeft++;
      } else {
        long numsRemainingInLeftArray = nLeft - iLeft;
        splitInversions += numsRemainingInLeftArray;
        mergedList.add(sortedRightList.get(iRight));
        iRight++;
      }
    }

    mergedList.addAll(sortedLeftList.subList(iLeft,nLeft));
    mergedList.addAll(sortedRightList.subList(iRight, nRight));

    return splitInversions;
  }

  public static List<Integer> readFileIntoIntegerList(String file) {
    return readFileIntoList(file).stream()
        .map(Integer::parseInt).collect(Collectors.toList());
  }

  public static List<String> readFileIntoList(String file) {
    List<String> lines = Collections.emptyList();
    try {
      lines = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return lines;
  }
}
