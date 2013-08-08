package info.tregmine;

import java.io.*;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.DirectoryScanner;

public class DelegateGenTask extends Task
{
    public String srcInterface;
    public String dstClass;
    public Path outputDir;

    public DelegateGenTask()
    {
    }

    @Override
    public void execute()
    throws BuildException
    {
        System.out.println("Generating " + dstClass);

        String[] outDirs = outputDir.list();
        if (outDirs.length != 1) {
            throw new BuildException("Only one output dir may be specified.");
        }

        File outDir = new File(outDirs[0]);

        String rep = File.separator;
        if ("\\".equals(rep)) {
            rep = "\\\\";
        }
        String subPath = dstClass.replaceAll("\\.", rep) + ".java";
        File targetFile = new File(outDir, subPath);

        try {
            PrintStream stream = new PrintStream(targetFile);
            DelegateGen.generateDelegate(stream, srcInterface, dstClass);
            stream.close();
        }
        catch (Exception e) {
            throw new BuildException(e);
        }
    }

    public void setSourceinterface(String srcInterface)
    {
        this.srcInterface = srcInterface;
    }

    public void setDestinationclass(String dstClass)
    {
        this.dstClass = dstClass;
    }

    public void setOutputdir(Path path)
    {
        this.outputDir = path;
    }
}
